package com.gjjbook.dao.factory;

import com.gjjbook.dao.*;
import com.gjjbook.dao.connectionPool.ConcurrentConnectionPool;
import com.gjjbook.dao.connectionPool.ConnectionPool;
import com.gjjbook.dao.connectionPool.JndiConnectionPool;
import com.gjjbook.domain.Account;
import com.gjjbook.domain.Group;
import com.gjjbook.domain.Phone;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DbDaoFactory implements DaoFactory<ConnectionPool> {
    private final ConnectionPool connectionPool;
    private Map<Class, DAOCreator> creators;

    public DbDaoFactory() throws DaoException {
//        connectionPool = ConcurrentConnectionPool.getInstance(); // done: 04.09.2017 сделать тесты
        connectionPool = JndiConnectionPool.getInstance();
        fillCreators();
    }

    @Override
    public void close() throws IOException {
        connectionPool.close();
    }

    private void fillCreators() {
        creators = new HashMap<Class, DAOCreator>();
        creators.put(Group.class, new DAOCreator<ConnectionPool>() {
            @Override
            public GenericDao create(ConnectionPool connectionPool) {
                return new GroupDao(connectionPool);
            }
        });
        creators.put(Account.class, new DAOCreator<ConnectionPool>() {
            @Override
            public GenericDao create(ConnectionPool connectionPool) {
                return new AccountDao(connectionPool);
            }
        });
        creators.put(Phone.class, new DAOCreator<ConnectionPool>() {
            @Override
            public GenericDao create(ConnectionPool connectionPool) {
                return new PhoneDao(connectionPool);
            }
        });
    }

    public ConnectionPool getContext() throws DaoException {
        return connectionPool;
    }

    @Override
    public GenericDao getDao(ConnectionPool connectionPool, Class dtoClass) throws DaoException {
        DAOCreator creator = creators.get(dtoClass);
        if (creator == null) {
            throw new DaoException("Dao object for " + dtoClass + " not found.");
        }
        return creator.create(connectionPool);
    }
}
