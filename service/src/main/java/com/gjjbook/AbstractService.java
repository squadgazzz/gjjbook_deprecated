package com.gjjbook;

import com.gjjbook.dao.GenericDao;
import com.gjjbook.dao.DaoException;
import com.gjjbook.dao.factory.DaoFactory;
import com.gjjbook.dao.factory.DbDaoFactory;
import com.gjjbook.domain.Account;
import com.gjjbook.domain.Identified;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public abstract class AbstractService<T extends Identified<PK>, PK extends Integer> implements Closeable {
    private final DaoFactory<Connection> factory;
    protected final GenericDao<T, PK> daoObject;

    public AbstractService() throws ServiceException { // DONE 16.07.2017 create service exception
        try {
            factory = new DbDaoFactory();
            daoObject = factory.getDao(factory.getContext(), Account.class);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public AbstractService(DaoFactory<Connection> factory, GenericDao<T, PK> daoObject) {
        this.factory = factory;
        this.daoObject = daoObject;
    }

    public abstract T create(T object) throws ServiceException;

    public abstract void update(T object) throws ServiceException;

    public abstract void delete(T object) throws ServiceException;

    public abstract T getByPk(PK id) throws ServiceException;

    public abstract List<T> getAll() throws ServiceException;

    @Override
    public void close() throws IOException {
        factory.close();
    }
}
