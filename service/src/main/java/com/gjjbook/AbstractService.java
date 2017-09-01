package com.gjjbook;

import com.gjjbook.dao.DaoException;
import com.gjjbook.dao.GenericDao;
import com.gjjbook.dao.connectionPool.ConnectionPool;
import com.gjjbook.dao.factory.DaoFactory;
import com.gjjbook.dao.factory.DbDaoFactory;
import com.gjjbook.domain.Identified;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractService<T extends Identified<PK>, PK extends Integer> implements Closeable {
    protected final DaoFactory<ConnectionPool> factory;
    protected final GenericDao<T, PK> daoObject;

    public AbstractService() throws ServiceException {
        try {
            factory = new DbDaoFactory();
            daoObject = getDaoObject();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public AbstractService(DaoFactory<ConnectionPool> factory, GenericDao<T, PK> daoObject) {
        this.factory = factory;
        this.daoObject = daoObject;
    }

    protected abstract GenericDao<T, PK> getDaoObject() throws ServiceException;

    public T create(T object) throws ServiceException {
        if (object == null) {
            return null;
        }

        ConnectionPool connectionPool = null;
        Connection connection = null;
        try {
            connectionPool = factory.getContext();
            connection = connectionPool.getConnection();
            connection.setAutoCommit(false);
            T result = daoObject.create(object);
            connection.commit();
            return result;
        } catch (DaoException | SQLException e) {
            throw new ServiceException(e);
        } finally {
            rollback(connection); // done: 30.08.2017 переместить роллбэк в файналли
            renewConnection(connectionPool, connection);
        }
    }

    public void update(T object) throws ServiceException {
        if (object == null) {
            return;
        }

        ConnectionPool connectionPool = null;
        Connection connection = null;
        try {
            connectionPool = factory.getContext();
            connection = connectionPool.getConnection();
            connection.setAutoCommit(false);
            daoObject.update(object);
            connection.commit();
        } catch (DaoException | SQLException e) {
            throw new ServiceException(e);
        } finally {
            rollback(connection);
            renewConnection(connectionPool, connection);
        }
    }

    public void delete(T object) throws ServiceException {
        if (object == null) {
            return;
        }

        ConnectionPool connectionPool = null;
        Connection connection = null;
        try {
            connectionPool = factory.getContext();
            connection = connectionPool.getConnection();
            connection.setAutoCommit(false);
            daoObject.delete(object);
            connection.commit();
        } catch (DaoException | SQLException e) {
            throw new ServiceException(e);
        } finally {
            rollback(connection);
            renewConnection(connectionPool, connection);
        }
    }

    public T getByPk(PK id) throws ServiceException {
        if (id == null) {
            return null;
        }

        ConnectionPool connectionPool = null;
        Connection connection = null;
        try {
            connectionPool = factory.getContext();
            connection = connectionPool.getConnection();
            connection.setAutoCommit(false);
            T result = daoObject.getByPK(id);
            connection.commit();
            return result;
        } catch (DaoException | SQLException e) {
            throw new ServiceException(e);
        } finally {
            rollback(connection);
            renewConnection(connectionPool, connection);
        }
    }

    public List<T> getAll() throws ServiceException {
        ConnectionPool connectionPool = null;
        Connection connection = null;
        try {
            connectionPool = factory.getContext();
            connection = connectionPool.getConnection();
            connection.setAutoCommit(false);
            List<T> result = daoObject.getAll();
            connection.commit();
            return result;
        } catch (DaoException | SQLException e) {
            throw new ServiceException(e);
        } finally {
            rollback(connection);
            renewConnection(connectionPool, connection);
        }
    }

    @Override
    public void close() throws IOException {
        factory.close();
    }

    protected void renewConnection(ConnectionPool connectionPool, Connection connection) throws ServiceException {
        if (connection != null) {
            try {
                connection.setAutoCommit(true);
                connectionPool.recycle(connection);
            } catch (SQLException | DaoException e) {
                throw new ServiceException(e);
            }
        }
    }

    protected void rollback(Connection connection) throws ServiceException {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException ee) {
                throw new ServiceException(ee);
            }
        }
    }
}
