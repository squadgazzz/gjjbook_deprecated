package com.gjjbook.dao.connectionPool;

import com.gjjbook.dao.DaoException;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

public class ConnectionPool implements Closeable {
    private static ConnectionPool instance;
    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();
    private final String user;
    private final String password;
    private final String url;
    private final Queue<Connection> freeConnections = new LinkedList<>();
    private final Queue<Connection> busyConnections = new LinkedList<>();
    private final int maxConnections;

    private ConnectionPool(String driver, String user, String password, String url, int maxConnections) {
        this.user = user;
        this.password = password;
        this.url = url;
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.maxConnections = maxConnections;
    }

    public synchronized static ConnectionPool getInstance(String driver, String user, String password, String url, int maxConnections) {
        if (instance == null) {
            instance = new ConnectionPool(driver, user, password, url, maxConnections);
        }
        return instance;
    }

    public synchronized Connection getConnection() throws DaoException {
        Connection threadConnection = connectionThreadLocal.get();
        if (threadConnection == null) {
            if (freeConnections.size() > 0) {
                Connection connection = freeConnections.poll();
                try {
                    if (connection.isClosed()) {
                        return getConnection();
                    } else {
                        busyConnections.add(connection);
                        connectionThreadLocal.set(connection);

                        return connection;
                    }
                } catch (SQLException e) {
                    throw new DaoException(e);
                }
            } else if (getTotalCount() < maxConnections) {
                Connection connection = createConnection();
                busyConnections.add(connection);
                connectionThreadLocal.set(connection);

                return connection;
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new DaoException(e);
                }
            }
        } else {
            if (freeConnections.remove(threadConnection)) {
                busyConnections.add(threadConnection);
            }

            return threadConnection;
        }

        return getConnection();
    }

    private synchronized int getTotalCount() {
        return freeConnections.size() + busyConnections.size();
    }

    private synchronized Connection createConnection() throws DaoException {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public synchronized void recycle(Connection connection) throws DaoException {
        if (busyConnections.remove(connection)) {
            freeConnections.add(connection);
            notifyAll();
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
        close(busyConnections);
    }
}
