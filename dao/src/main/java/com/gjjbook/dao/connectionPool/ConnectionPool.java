package com.gjjbook.dao.connectionPool;

import com.gjjbook.dao.PersistException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Semaphore;

public class ConnectionPool {
    private String user;
    private String password;
    private String url;
    private final Semaphore semaphore;

    public ConnectionPool(String driver, String user, String password, String url, int connectionsCount) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.user = user;
        this.password = password;
        this.url = url;
        semaphore = new Semaphore(connectionsCount);
    }

    public synchronized Connection getConnection() throws PersistException {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new PersistException(e);
        }
    }

    public synchronized void release(Connection connection) throws PersistException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new PersistException(e);
        } finally {
            semaphore.release();
        }
    }
}
