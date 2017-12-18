package com.gjjbook.service;

import com.gjjbook.dao.AccountDao;
import com.gjjbook.dao.DaoException;
import com.gjjbook.dao.FriendDao;
import com.gjjbook.dao.PhoneDao;
import com.gjjbook.domain.Account;
import com.gjjbook.domain.DTO.AccountDTO;
import com.gjjbook.domain.Friend;
import com.gjjbook.domain.FriendPk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService implements Serviceable<Account, Integer> {
    private AccountDao accountDao;
    private PhoneDao phoneDao;
    private FriendDao friendDao;

    public AccountService() {
    }

    @Autowired
    public AccountService(AccountDao accountDao, PhoneDao phoneDao, FriendDao friendDao) {
        this.accountDao = accountDao;
        this.phoneDao = phoneDao;
        this.friendDao = friendDao;
    }

    @Transactional
    public void requestFriend(Account first, Account second) throws DaoException {
        friendDao.requestFriend(first, second);
    }

    @Transactional
    public void acceptFriend(Account first, Account second) throws DaoException {
        friendDao.acceptFriend(first, second);
    }

    @Transactional
    public void removeFriend(Account first, Account second) throws DaoException {
        friendDao.removeFriend(first, second);
    }

    @Transactional
    public void declineFriend(Account first, Account second) throws DaoException {
        friendDao.declineFriend(first, second);
    }

    public Friend getFriendByPk(Account accountOne, Account accountTwo) throws DaoException {
        return friendDao.getByPK(new FriendPk(accountOne, accountTwo));
    }

    public String getFriendStatus(Integer first, Integer second) {
        return friendDao.getStatus(first, second);
    }


    public String getFriendStatus(Account first, Account second) {
        return friendDao.getStatus(first, second);
    }

    @Override
    @Transactional
    public Account update(Account account) throws DaoException {
        if (account == null) {
            return null;
        }

        return accountDao.update(account);
    }

    @Override
    @Transactional
    public void delete(Account account) {
        if (account == null) {
            return;
        }

        accountDao.delete(account);
    }

    @Override
    public Account getByPk(Integer id) {
        if (id == null) {
            return null;
        }

        return accountDao.getByPK(id);
    }

    @Override
    public List<Account> getAll() {
        return accountDao.getAll();
    }

    public Account getByEmail(String email) {
        if (email == null) {
            return null;
        }

        return accountDao.getByEmail(email);
    }

    public boolean isPasswordMatch(String email, String password, boolean isEncrypted) {
        return !(email == null || password == null) && accountDao.isPasswordMatch(email, password, isEncrypted);
    }

    public List<AccountDTO> findByPartName(String query, int currentPage, int pageSize) {
        if (query == null) {
            return null;
        }

        return accountDao.findByPartName(query, currentPage, pageSize);
    }

    public long getSearchResultCount(String query) {
        if (query == null) {
            return 0;
        }

        return accountDao.getSearchResultCount(query);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountService that = (AccountService) o;

        if (accountDao != null ? !accountDao.equals(that.accountDao) : that.accountDao != null) return false;
        return phoneDao != null ? phoneDao.equals(that.phoneDao) : that.phoneDao == null;
    }

    @Override
    public int hashCode() {
        int result = accountDao.hashCode();
        result = 31 * result + phoneDao.hashCode();
        result = 31 * result + friendDao.hashCode();
        return result;
    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public PhoneDao getPhoneDao() {
        return phoneDao;
    }

    public void setPhoneDao(PhoneDao phoneDao) {
        this.phoneDao = phoneDao;
    }

    public FriendDao getFriendDao() {
        return friendDao;
    }

    public void setFriendDao(FriendDao friendDao) {
        this.friendDao = friendDao;
    }

    public List<Account> findAccountFriends(Account account, int currentPage, int pageSize) {
        return friendDao.getAccountFriends(account, currentPage, pageSize);
    }

    public List<Account> getAccountOutRequests(Account account, int currentPage, int pageSize) {
        return friendDao.getAccountOutRequests(account, currentPage, pageSize);
    }

    public List<Account> getAccountInRequests(Account account, int currentPage, int pageSize) {
        return friendDao.getAccountInRequests(account, currentPage, pageSize);
    }
}