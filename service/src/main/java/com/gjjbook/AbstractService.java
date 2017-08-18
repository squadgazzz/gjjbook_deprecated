package com.gjjbook;

import com.gjjbook.dao.DaoException;
import com.gjjbook.dao.GenericDao;
import com.gjjbook.dao.factory.DaoFactory;
import com.gjjbook.dao.factory.DbDaoFactory;
import com.gjjbook.domain.Identified;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public abstract class AbstractService<T extends Identified<PK>, PK extends Integer> implements Closeable {
    protected final DaoFactory<Connection> factory;
    protected final GenericDao<T, PK> daoObject;

    public AbstractService() throws ServiceException {
        try {
            factory = new DbDaoFactory();
            daoObject = getDaoObject();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    protected abstract GenericDao<T, PK> getDaoObject() throws ServiceException;

    public AbstractService(DaoFactory<Connection> factory, GenericDao<T, PK> daoObject) {
        this.factory = factory;
        this.daoObject = daoObject;
    }

    public T create(T object) throws ServiceException {
        if (object == null) {
            return null;
        }

        try {
            return daoObject.create(object);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void update(T object) throws ServiceException {
        if (object == null) {
            return;
        }

        try {
            daoObject.update(object);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void delete(T object) throws ServiceException {
        if (object == null) {
            return;
        }

        try {
            daoObject.delete(object);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public T getByPk(PK id) throws ServiceException {
        if (id == null) {
            return null;
        }

        try {
            return daoObject.getByPK(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<T> getAll() throws ServiceException {
        try {
            return daoObject.getAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void close() throws IOException {
        factory.close();
    }
}
