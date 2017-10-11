package com.gjjbook.dao;

import com.gjjbook.domain.Account;
import com.gjjbook.domain.Sex;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.io.FileReader;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dao-context.xml", "classpath:dao-context-overrides.xml"})
public class AccountDaoTest {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private DataSource dataSource;

    @Before
    public void setUp() throws Exception, DaoException {
        Connection connection = dataSource.getConnection();
        FileReader fr = new FileReader("src/test/resources/createAccounts.sql");
        RunScript.execute(connection, fr);
        fr = new FileReader("src/test/resources/createPhones.sql");
        RunScript.execute(connection, fr);
        fr = new FileReader("src/test/resources/createFriends.sql");
        RunScript.execute(connection, fr);
        fr = new FileReader("src/test/resources/createEmailPassword.sql");
        RunScript.execute(connection, fr);
        connection.close();
    }

    @After
    public void tearDown() throws Exception, DaoException {
        Connection connection = dataSource.getConnection();
        FileReader fr = new FileReader("src/test/resources/dropDb.sql");
        RunScript.execute(connection, fr);
        connection.close();
    }

    @Test
    public void getDao() throws Exception, DaoException {
        Assert.assertNotNull(accountDao);
    }

    @Test
    public void create() throws Exception, DaoException {
        Account newAcc = createTestAccount();

        Assert.assertNotNull(newAcc);
    }

    @Test
    public void getByPK() throws Exception, DaoException {
        Account newAcc = createTestAccount();

        Assert.assertEquals(newAcc, accountDao.getByPK(1));

    }

    @Test
    public void update() throws Exception, DaoException {
        Account newAcc = createTestAccount();
        newAcc.setName("petr");
        accountDao.update(newAcc);

        Assert.assertEquals("petr", accountDao.getByPK(1).getName());
    }

    @Test
    public void delete() throws Exception, DaoException {
        Account newAcc = createTestAccount();
        accountDao.delete(newAcc);

        Assert.assertNull(accountDao.getByPK(1));
    }

    @Test
    public void getAll() throws Exception, DaoException {
        List<Account> list = new LinkedList<>();
        list.add(createTestAccount("1"));
        list.add(createTestAccount("2"));

        Assert.assertEquals(list, accountDao.getAll());
    }

    private Account createTestAccount(String email) throws DaoException {
        byte[] avatar = {1, 2, 3};
        Account account = new Account(avatar, "ivan", null, "ivanov",
                Sex.MALE, LocalDate.of(2000, 10, 20),
                null, "home", "work",
                email, "7894", "skype",
                null, null, null);

        return accountDao.create(account);
    }

    private Account createTestAccount() throws DaoException {
        return createTestAccount("email");
    }
}