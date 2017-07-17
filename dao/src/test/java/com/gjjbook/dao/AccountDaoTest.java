package com.gjjbook.dao;

import com.gjjbook.dao.factory.DaoFactory;
import com.gjjbook.dao.factory.DbDaoFactory;
import com.gjjbook.domain.Account;
import com.gjjbook.domain.Phone;
import com.gjjbook.domain.PhoneType;
import com.gjjbook.domain.Sex;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class AccountDaoTest {
    private DaoFactory<Connection> daoFactory;
    private Connection connection;
    private GenericDao<Account, Integer> accountDao;

    @Before
    public void setUp() throws Exception, DaoException {
        daoFactory = new DbDaoFactory();
        connection = daoFactory.getContext();
        RunScript runScript = new RunScript();
        FileReader fr = new FileReader("src/test/resources/createAccounts.sql");
        runScript.execute(connection, fr);
        fr = new FileReader("src/test/resources/createPhones.sql");
        runScript.execute(connection, fr);
        fr = new FileReader("src/test/resources/createFriends.sql");
        runScript.execute(connection, fr);
        accountDao = daoFactory.getDao(connection, Account.class);
    }

    @After
    public void tearDown() throws Exception {
        daoFactory.close();
    }

    @Test
    public void getContext() throws Exception, DaoException {
        Assert.assertNotNull(connection);
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
        Phone phoneA = new Phone(PhoneType.HOME, 7, 4959998877L);
        Phone phoneB = new Phone(PhoneType.WORK, 7, 4951112233L);
        List<Phone> phones = new LinkedList<>();
        phones.add(phoneA);
        phones.add(phoneB);

        Account account = new Account("ivan", null, "ivanov",
                Sex.MALE, LocalDate.of(2000, 10, 20),
                phones, "home", "work",
                email, 7894, "skype",
                null, null, null);

        return accountDao.create(account);
    }

    private Account createTestAccount() throws DaoException {
        return createTestAccount("email");
    }
}