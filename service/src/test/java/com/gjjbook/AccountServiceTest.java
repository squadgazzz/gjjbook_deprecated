package com.gjjbook;

import com.gjjbook.dao.AccountDao;
import com.gjjbook.dao.DaoException;
import com.gjjbook.dao.FriendDao;
import com.gjjbook.dao.PhoneDao;
import com.gjjbook.domain.Account;
import com.gjjbook.domain.Phone;
import com.gjjbook.service.AccountService;
import com.gjjbook.service.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {
    @Mock
    private Account account;

    @Mock
    private Phone phone;

    @Mock
    private AccountDao accountDao;

    @Mock
    private PhoneDao phoneDao;

    @Mock
    private FriendDao friendDao;

    @InjectMocks
    private AccountService accountService;

    @Before
    public void setUp() throws Exception, ServiceException, DaoException {
        accountService = new AccountService(accountDao, phoneDao, friendDao);
        when(accountDao.update(new Account())).thenReturn(account);
    }

    @Test
    public void create() throws Exception, DaoException, ServiceException {
        assertEquals(account, accountService.update(new Account()));
        assertNotEquals(mock(Account.class), accountService.update(new Account()));

        assertEquals(null, accountService.update(null));

        verify(accountDao, times(2)).update(new Account());
        verify(accountDao, times(0)).update(null);
    }

    @Test
    public void update() throws Exception, DaoException, ServiceException {
        accountService.update(account);

        verify(accountDao).update(account);
    }

    @Test
    public void delete() throws Exception, DaoException, ServiceException {
        accountService.delete(account);

        verify(accountDao).delete(account);
    }

    @Test
    public void getByPk() throws Exception, DaoException, ServiceException {
        when(accountDao.getByPK(1)).thenReturn(account);
        List<Phone> phoneList = new ArrayList<>();
        phoneList.add(phone);
//        when(phoneDao.getPhonesByAccountId(1)).thenReturn(phoneList);

        assertEquals(account, accountService.getByPk(1));
        assertNotEquals(mock(Account.class), accountService.getByPk(1));
        assertEquals(null, accountService.getByPk(null));

        verify(accountDao, times(2)).getByPK(1);
        verify(accountDao, times(0)).getByPK(null);
    }

    @Test
    public void getAll() throws Exception, DaoException, ServiceException {
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);

        when(accountDao.getAll()).thenReturn(accounts);

        assertEquals(accounts, accountService.getAll());

        verify(accountDao).getAll();
    }
}