package com.gjjbook;

import com.gjjbook.dao.GenericDao;
import com.gjjbook.dao.PersistException;
import com.gjjbook.dao.factory.DaoFactory;
import com.gjjbook.domain.Account;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @Mock
    private Account account;

    @Mock
    private GenericDao<Account, Integer> accountDao;

    @Mock
    private DaoFactory<Connection> factory;

    @InjectMocks
    private AccountService accountService;

    @Before
    public void setUp() throws Exception, PersistException {
        MockitoAnnotations.initMocks(this);

        Connection connectionMock = mock(Connection.class);
        when(factory.getContext()).thenReturn(connectionMock);
        when(factory.getDao(factory.getContext(), Account.class)).thenReturn(accountDao);

        accountService = new AccountService(factory, accountDao);
    }

    @After
    public void tearDown() throws Exception {
        accountService.close();
    }

    @Test
    public void create() throws Exception, PersistException {
        when(accountDao.create(new Account())).thenReturn(account);

        assertEquals(account, accountService.create(new Account()));
        assertNotEquals(mock(Account.class), accountService.create(new Account()));

        assertEquals(null, accountService.create(null));

        verify(accountDao, times(2)).create(new Account());
        verify(accountDao, times(0)).create(null);
    }

    @Test
    public void update() throws Exception, PersistException {
        accountService.update(account);

        verify(accountDao).update(account);
    }

    @Test
    public void delete() throws Exception, PersistException {
        accountService.delete(account);

        verify(accountDao).delete(account);
    }

    @Test
    public void getByPk() throws Exception, PersistException {
        when(accountDao.getByPK(1)).thenReturn(account);

        assertEquals(account, accountService.getByPk(1));
        assertNotEquals(mock(Account.class), accountService.getByPk(1));
        assertEquals(null, accountService.getByPk(null));

        verify(accountDao, times(2)).getByPK(1);
        verify(accountDao, times(0)).getByPK(null);
    }

    @Test
    public void getAll() throws Exception, PersistException {
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);

        when(accountDao.getAll()).thenReturn(accounts);

        assertEquals(accounts, accountService.getAll());

        verify(accountDao).getAll();
    }

    @Test
    public void addFriend() throws Exception, PersistException {
        Account friend = mock(Account.class);
        List<Account> friends = new ArrayList<>();
        friends.add(friend);
        List<Account> friendFriends = new ArrayList<>();
        friendFriends.add(account);
        accountService.addFriend(account, friend);

        verify(accountDao).update(account);
        verify(account).setFriendList(friends);
        verify(accountDao).update(friend);
        verify(friend).setFriendList(friendFriends);
    }

    @Test
    public void removeFriend() throws Exception, PersistException {
        Account friendOne = new Account();
        friendOne.setEmail("1");
        Account friendTwo = new Account();
        friendTwo.setEmail("2");
        List<Account> oneFriends = new ArrayList<>();
        List<Account> twoFriends = new ArrayList<>();
        oneFriends.add(friendTwo);
        twoFriends.add(friendOne);
        friendOne.setFriendList(oneFriends);
        friendTwo.setFriendList(twoFriends);

        accountService.removeFriend(friendOne, friendTwo);

        verify(accountDao).update(friendOne);
        verify(accountDao).update(friendTwo);
    }

    @Test
    public void getFriends() throws Exception {
        accountService.getFriends(account);

        verify(account).getFriendList();
    }

    @Test
    public void close() throws Exception {
        accountService.close();

        verify(factory).close();
    }
}