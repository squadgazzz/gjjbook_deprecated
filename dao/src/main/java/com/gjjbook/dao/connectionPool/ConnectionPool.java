package com.gjjbook.dao.connectionPool;

import com.gjjbook.dao.DaoException;

import java.io.Closeable;
import java.sql.Connection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class ConnectionPool implements Closeable {
    protected static final Lock LOCK = new ReentrantLock();
    protected static final ThreadLocal<Connection> CONNECTION_THREAD_LOCAL = new ThreadLocal<>();

    public abstract Connection getConnection() throws DaoException;

    public abstract void recycle(Connection connection) throws DaoException;

}
