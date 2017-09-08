package com.gjjbook.dao.connectionPool;

import com.gjjbook.dao.DaoException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class ConcurrentConnectionPool extends ConnectionPool {
    private static ConcurrentConnectionPool instance;

    private final Semaphore semaphore;
    private final BlockingQueue<Connection> freeConnections = new LinkedBlockingQueue<>();
    private final BlockingQueue<Connection> busyConnections = new LinkedBlockingQueue<>();
    private String driver;
    private String user;
    private String password;
    private String url;
    private int connectionsCount;

    private ConcurrentConnectionPool() throws DaoException {
        try {
            setDbProperties();
            Class.forName(driver);
        } catch (ClassNotFoundException | DaoException e) {
            throw new DaoException(e);
        }
        semaphore = new Semaphore(connectionsCount);
    }

    public static ConnectionPool getInstance() throws DaoException {
        try {
            LOCK.lock();
            if (instance == null) {
                instance = new ConcurrentConnectionPool();
            }
            return instance;
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public Connection getConnection() throws DaoException {
        Connection connection = CONNECTION_THREAD_LOCAL.get();
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }

            semaphore.acquire();
            connection = freeConnections.poll();
            if (connection != null) {
                setNewConnection(connection);
                return connection;
            } else {
                return createConnection();
            }
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    private Connection createConnection() throws DaoException {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            setNewConnection(connection);
            return connection;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void setNewConnection(Connection connection) {
        CONNECTION_THREAD_LOCAL.set(connection);
        if (!busyConnections.contains(connection)) {
            busyConnections.add(connection);
        }
    }

    @Override
    public void recycle(Connection connection) throws DaoException {
        if (CONNECTION_THREAD_LOCAL.get() != null) {
            try {
                LOCK.lock();
                if (!connection.isClosed() && !freeConnections.contains(connection)) {
                    freeConnections.offer(connection);
                } else {
                    freeConnections.remove(connection);
                }
                busyConnections.remove(connection);
                CONNECTION_THREAD_LOCAL.set(null);

                if (semaphore.availablePermits() < connectionsCount) {
                    semaphore.release();
                }
            } catch (SQLException e) {
                throw new DaoException(e);
            } finally {
                LOCK.unlock();
            }
        }
    }

    private void close(Queue<Connection> connections) {
        if (connections.size() > 0) {
            connections.forEach(
                    c -> {
                        try {
                            if (!c.isClosed()) {
                                c.close();
                                connections.remove(c);
                            }
                            if (semaphore.availablePermits() < connectionsCount) {
                                semaphore.release();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }
    }

    @Override
    public void close() throws IOException {
        close(freeConnections);
        close(busyConnections);
    }

    private void setDbProperties() throws DaoException {
        try (InputStreamReader is = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("db.properties"))) {
            Properties properties = new Properties();

            properties.load(is);
            url = properties.getProperty("db.url");
            user = properties.getProperty("db.login");
            password = properties.getProperty("db.password");
            driver = properties.getProperty("jdbc.driver");
            connectionsCount = Integer.parseInt(properties.getProperty("jdbc.connections_count"));
        } catch (IOException e) {
            throw new DaoException(e);
        }
    }
}