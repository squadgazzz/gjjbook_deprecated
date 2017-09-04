package com.gjjbook.dao.factory;

import com.gjjbook.dao.*;
import com.gjjbook.dao.connectionPool.ConcurrentConnectionPool;
import com.gjjbook.dao.connectionPool.ConnectionPool;
import com.gjjbook.dao.connectionPool.JndiConnectionPool;
import com.gjjbook.domain.Account;
import com.gjjbook.domain.Group;
import com.gjjbook.domain.Phone;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DbDaoFactory implements DaoFactory<ConnectionPool> {
    private final ConnectionPool connectionPool;
    private String driver;
    private String user;
    private String password;
    private String url;
    private int connectionsCount;
    private Map<Class, DAOCreator> creators;

    public DbDaoFactory() throws DaoException {
        setDbProperties();
        connectionPool = ConcurrentConnectionPool.getInstance(driver, user, password, url, connectionsCount);
//        connectionPool = JndiConnectionPool.getInstance();
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

    private void setDbProperties() throws DaoException {
        try (InputStreamReader is = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("db.properties"))) {
            Properties properties = new Properties();
            properties.load(is);

            url = properties.getProperty("db.url");
            user = properties.getProperty("db.login");
            password = properties.getProperty("db.password");
            driver = properties.getProperty("jdbc.driver");
            connectionsCount = Integer.parseInt(properties.getProperty("jdbc.connections_count"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
