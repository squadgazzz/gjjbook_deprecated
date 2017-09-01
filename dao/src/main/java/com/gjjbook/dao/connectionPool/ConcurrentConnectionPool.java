package com.gjjbook.dao.connectionPool;

import com.gjjbook.dao.DaoException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class ConcurrentConnectionPool extends ConnectionPool {
    private static ConcurrentConnectionPool instance;
    private final Semaphore semaphore;
    private final String user;
    private final String password;
    private final String url;
    private final BlockingQueue<Connection> freeConnections = new LinkedBlockingQueue<>();

    private ConcurrentConnectionPool(String driver, String user, String password, String url, int maxConnections) {
        this.user = user;
        this.password = password;
        this.url = url;
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        semaphore = new Semaphore(maxConnections);
    }

    public static ConnectionPool getInstance(String driver, String user, String password, String url, int maxConnections) {
        try {
            LOCK.lock();
            if (instance == null) {
                instance = new ConcurrentConnectionPool(driver, user, password, url, maxConnections);
            }
            return instance;
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public Connection getConnection() throws DaoException { // done: 30.08.2017 уменьшить синхронизацию
        Connection connection = CONNECTION_THREAD_LOCAL.get();
        if (connection != null) {
            return connection;
        }

        try {
            semaphore.acquire();
            LOCK.lock();
            connection = freeConnections.poll();
            if (connection != null) {
                CONNECTION_THREAD_LOCAL.set(connection);
                return connection;
            } else {
                return createConnection();
            }
        } catch (InterruptedException e) {
            throw new DaoException(e);
        } finally {
            LOCK.unlock();
        }
    }

    private Connection createConnection() throws DaoException {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            CONNECTION_THREAD_LOCAL.set(connection);
            return connection;
        } catch (SQLException e) {
            throw new DaoException(e);
        }

    }

    @Override
    public void recycle(Connection connection) throws DaoException {
        try {
            LOCK.lock();
            if (!connection.isClosed()) {
                freeConnections.offer(connection);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            semaphore.release();
            LOCK.unlock();
        }
    }

    private void close(Queue<Connection> connections) {
        if (connections.size() > 0) {
            for (Connection c : connections) {
                try {
                    if (!c.isClosed()) {
                        c.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        close(freeConnections);
    }
}
