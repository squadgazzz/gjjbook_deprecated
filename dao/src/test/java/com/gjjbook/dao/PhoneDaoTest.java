package com.gjjbook.dao;

import com.gjjbook.domain.Account;
import com.gjjbook.domain.Phone;
import com.gjjbook.domain.PhoneType;
import com.gjjbook.domain.Sex;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.FileReader;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dao-context.xml", "classpath:dao-context-overrides.xml"})
public class PhoneDaoTest {

    @Autowired
    private PhoneDao phoneDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private DataSource dataSource;

    @Before
    public void setUp() throws Exception, DaoException {
        Connection connection = dataSource.getConnection();
        FileReader fr = new FileReader("src/test/resources/createAccounts.sql");
        RunScript.execute(connection, fr);
        fr = new FileReader("src/test/resources/insertTestAccount.sql");
        RunScript.execute(connection, fr);
        fr = new FileReader("src/test/resources/createPhones.sql");
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
        Assert.assertNotNull(phoneDao);
    }

    @Test
    @Transactional
    public void create() throws Exception, DaoException {
        Phone newPhone = createTestPhone();

        Assert.assertNotNull(newPhone);
        Assert.assertEquals(1, newPhone.getId());
        Assert.assertEquals("ivan", newPhone.getOwner().getName());
        Account account = accountDao.getByPK(1);
        Assert.assertEquals("ivan", account.getName());
        List<Phone> phones = account.getPhones();
        if (phones == null) {
            System.out.println("!!!!!!!!!!null");
        } else if (phones.size() == 0) {
            System.out.println("!!!!!!!!!!!!!0");
        } else {
            System.out.println("!!!!!!!!!!!!!" + Arrays.toString(phones.toArray()));
        }
//        Assert.assertEquals("12345", newPhone.getOwner().getPhones().get(0).getNumber());
    }

    @Test
    @Transactional
    public void getByPK() throws Exception, DaoException {
        Phone newPhone = createTestPhone();

        Assert.assertEquals(newPhone, phoneDao.getByPK(1));
    }

    @Test
    @Transactional
    public void update() throws Exception, DaoException {
        Phone newPhone = createTestPhone();
        newPhone.setNumber("999");
        phoneDao.update(newPhone);

        Assert.assertEquals("999", phoneDao.getByPK(1).getNumber());
    }

    @Test
    @Transactional
    public void delete() throws Exception, DaoException {
        Phone newPhone = createTestPhone();
        phoneDao.delete(newPhone);

        Assert.assertNull(phoneDao.getByPK(1));
    }

    @Test
    @Transactional
    public void getAll() throws Exception, DaoException {
        List<Phone> list = new LinkedList<>();
        list.add(createTestPhone("1"));
        list.add(createTestPhone("2"));

        Assert.assertEquals(list, phoneDao.getAll());
    }

    private Phone createTestPhone(String number) throws DaoException {
        Account account = new Account(null, "ivan", null, "ivanov",
                Sex.MALE, LocalDate.of(2000, 10, 20),
                null, "home", "work",
                number, "7894", "skype",
                null, null, "123");
        Account dbAccount = accountDao.getByPK(1);
        Phone phone = new Phone(dbAccount, PhoneType.MOBILE, number);

        Phone dbPhone = phoneDao.update(phone);
//        List<Phone> phones = new ArrayList<>();
//        phones.add(dbPhone);
//        dbAccount.setPhones(phones);
//        accountDao.update(account);

        return dbPhone;
    }

    private Phone createTestPhone() throws DaoException {
        return createTestPhone("12345");
    }
}
