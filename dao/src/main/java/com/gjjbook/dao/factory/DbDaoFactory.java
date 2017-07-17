package com.gjjbook.dao.factory;

import com.gjjbook.dao.*;
import com.gjjbook.dao.connectionPool.ConnectionPool;
import com.gjjbook.domain.Account;
import com.gjjbook.domain.Group;
import com.gjjbook.domain.Phone;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.*;

public class DbDaoFactory implements DaoFactory<Connection> {
    private String driver;
    private String user;
    private String password;
    private String url;
    private int connectionsCount;
    private Map<Class, DAOCreator> creators;
    private final ConnectionPool connectionPool;
    private final Queue<Connection> openedConnections = new LinkedList<>();

    public DbDaoFactory() throws DaoException {
        setDbProperties();
        connectionPool = new ConnectionPool(driver, user, password, url, connectionsCount);
        fillCreators();
    }

    @Override
    public void close() throws IOException {
        connectionPool.close();
    }

    private void fillCreators() {
        creators = new HashMap<Class, DAOCreator>();
        creators.put(Group.class, new DAOCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new GroupDao(connection);
            }
        });
        creators.put(Account.class, new DAOCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new AccountDao(connection);
            }
        });
        creators.put(Phone.class, new DAOCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new PhoneDao(connection);
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

    public Connection getContext() throws DaoException {
        Connection connection = connectionPool.getConnection();
        openedConnections.add(connection);
        return connection;
    }

    @Override
    public GenericDao getDao(Connection connection, Class dtoClass) throws DaoException {
        DAOCreator creator = creators.get(dtoClass);
        if (creator == null) {
            throw new DaoException("Dao object for " + dtoClass + " not found.");
        }
        return creator.create(connection);
    }
}
