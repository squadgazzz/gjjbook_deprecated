package com.gjjbook.dao.connectionPool;

import com.gjjbook.dao.DaoException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class JndiConnectionPool extends ConnectionPool {
    private static JndiConnectionPool instance;
    private final DataSource ds;

    private JndiConnectionPool() throws DaoException {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            ds = (DataSource) envContext.lookup("jdbc/gjjbook_db");
        } catch (NamingException e) {
            throw new DaoException(e);
        }
    }

    public static JndiConnectionPool getInstance() throws DaoException {
        try {
            LOCK.lock();
            if (instance == null) {
                instance = new JndiConnectionPool();
            }
            return instance;
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public Connection getConnection() throws DaoException {
        try {
            Connection connection = CONNECTION_THREAD_LOCAL.get();
            if (connection != null) {
                if (!connection.isClosed()) {
                    return connection;
                } else {
                    return createConnection();
                }
            } else {
                return createConnection();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Connection createConnection() throws SQLException {
        Connection connection = ds.getConnection();
        CONNECTION_THREAD_LOCAL.set(connection);
        return connection;
    }

    @Override
    public void recycle(Connection connection) throws DaoException {
        try {
            LOCK.lock();
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public void close() throws IOException {
        if (CONNECTION_THREAD_LOCAL != null) {
            try {
                CONNECTION_THREAD_LOCAL.get().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
