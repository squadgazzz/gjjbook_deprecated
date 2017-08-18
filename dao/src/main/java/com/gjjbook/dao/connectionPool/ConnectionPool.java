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
    private final String user;
    private final String password;
    private final String url;
    private final Queue<Connection> freeConnections = new LinkedList<>();
    private final Queue<Connection> busyConnections = new LinkedList<>();
    private final int maxConnections;

    public ConnectionPool(String driver, String user, String password, String url, int maxConnections) {
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

    public synchronized Connection getConnection() throws DaoException {
        Connection connection = null;
        if (freeConnections.size() > 0) {
            connection = freeConnections.poll();
        } else if (getTotalCount() < maxConnections) {
            connection = create();
        }

        if (connection != null) {
            busyConnections.add(connection);
        }

        return connection;
    }

    private synchronized int getTotalCount() {
        return freeConnections.size() + busyConnections.size();
    }

    private synchronized Connection create() throws DaoException {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public synchronized void recycle(Connection connection) throws DaoException {
        if (busyConnections.contains(connection)) {
            busyConnections.remove(connection);
        }

        if (!freeConnections.offer(connection)) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new DaoException(e);
            }
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
