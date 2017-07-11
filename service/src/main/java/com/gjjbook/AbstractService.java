package com.gjjbook;

import com.gjjbook.dao.GenericDao;
import com.gjjbook.dao.PersistException;
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

    public AbstractService() throws PersistException {
        factory = new DbDaoFactory();
        daoObject = factory.getDao(factory.getContext(), Account.class);
    }

    public AbstractService(DaoFactory<Connection> factory, GenericDao<T, PK> daoObject) {
        this.factory = factory;
        this.daoObject = daoObject;
    }

    public abstract T create(T object) throws PersistException;

    public abstract void update(T object) throws PersistException;

    public abstract void delete(T object) throws PersistException;

    public abstract T getByPk(PK id) throws PersistException;

    public abstract List<T> getAll() throws PersistException;

    @Override
    public void close() throws IOException {
        factory.close();
    }
}
