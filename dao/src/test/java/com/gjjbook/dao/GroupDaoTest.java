package com.gjjbook.dao;

import com.gjjbook.dao.connectionPool.ConnectionPool;
import com.gjjbook.dao.factory.DaoFactory;
import com.gjjbook.dao.factory.DbDaoFactory;
import com.gjjbook.domain.Group;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

public class GroupDaoTest {
    private DaoFactory<ConnectionPool> daoFactory;
    private ConnectionPool connectionPool;
    private GenericDao<Group, Integer> groupDao;

    @Before
    public void setUp() throws Exception, DaoException {
        daoFactory = new DbDaoFactory();
        connectionPool = daoFactory.getContext();
        Connection connection = connectionPool.getConnection();
        RunScript runScript = new RunScript();
        FileReader fr = new FileReader("src/test/resources/createGroups.sql");
        runScript.execute(connection, fr);
        groupDao = daoFactory.getDao(connectionPool, Group.class);
    }

    @After
    public void tearDown() throws Exception {
        daoFactory.close();
    }

    @Test
    public void getContext() throws Exception, DaoException {
        Assert.assertNotNull(connectionPool);
    }

    @Test
    public void getDao() throws Exception, DaoException {
        Assert.assertNotNull(groupDao);
    }

    @Test
    public void create() throws Exception, DaoException {
        Group newGroup = createTestGroup();

        Assert.assertNotNull(newGroup);
    }

    @Test
    public void getByPK() throws Exception, DaoException {
        Group newGroup = createTestGroup();

        Assert.assertEquals(newGroup, groupDao.getByPK(1));
    }

    @Test
    public void update() throws Exception, DaoException {
        Group newGroup = createTestGroup();
        newGroup.setName("petr");
        groupDao.update(newGroup);

        Assert.assertEquals("petr", groupDao.getByPK(1).getName());
    }

    @Test
    public void delete() throws Exception, DaoException {
        Group newGroup = createTestGroup();
        groupDao.delete(newGroup);

        Assert.assertNull(groupDao.getByPK(1));
    }

    @Test
    public void getAll() throws Exception, DaoException {
        List<Group> list = new LinkedList<>();
        list.add(createTestGroup("1"));
        list.add(createTestGroup("2"));

        Assert.assertEquals(list, groupDao.getAll());
    }

    private Group createTestGroup(String name) throws DaoException {
        Group group = new Group(name, "desc", null);
        return groupDao.create(group);
    }

    private Group createTestGroup() throws DaoException {
        return createTestGroup("name");
    }
}