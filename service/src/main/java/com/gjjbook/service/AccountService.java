package com.gjjbook.service;

import com.gjjbook.dao.AccountDao;
import com.gjjbook.dao.DaoException;
import com.gjjbook.dao.PhoneDao;
import com.gjjbook.domain.Account;
import com.gjjbook.domain.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class AccountService implements Serviceable<Account, Integer> {
    private AccountDao accountDao;
    private PhoneDao phoneDao;

    public AccountService() {
    }

    @Autowired
    public AccountService(AccountDao accountDao, PhoneDao phoneDao) {
        this.accountDao = accountDao;
        this.phoneDao = phoneDao;
    }

    @Override
    public Account create(Account account) throws ServiceException {
        if (account == null) {
            return null;
        }

        try {
            Account dbAccount = accountDao.create(account);
            int id = dbAccount.getId();
            List<Phone> phones = account.getPhones();
            if (phones != null && phones.size() > 0) {
                for (Phone p : phones) {
                    p.setOwnerId(id);
                    phoneDao.create(p);
                }
            }

            return dbAccount;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean update(Account account) throws ServiceException {
        if (account == null) {
            return false;
        }

        try {
            int id = account.getId();
            List<Phone> phonesFromDb = phoneDao.getPhonesByAccountId(id);
            List<Phone> phones = account.getPhones();
            accountDao.update(account);
            if (phones != null) {
                if (phones.size() > 0) {
                    for (Phone p : phones) {
                        p.setOwnerId(id);
                        if (phonesFromDb != null || phonesFromDb.size() > 0) {
                            int index = phonesFromDb.indexOf(p);
                            if (index < 0) {
                                phoneDao.create(p);
                            } else {
                                Phone oldPhone = phonesFromDb.get(index);
                                if (!p.getType().equals(oldPhone.getType())) {
                                    oldPhone.setType(p.getType());
                                    phoneDao.update(oldPhone);
                                }
                            }
                        } else {
                            phoneDao.create(p);
                        }
                    }

                    if (phonesFromDb != null) {
                        for (Phone ph : phonesFromDb) {
                            if (!phones.contains(ph)) {
                                phoneDao.delete(ph);
                            }
                        }
                    }
                } else {
                    for (Phone p : phonesFromDb) {
                        phoneDao.delete(p);
                    }
                }
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }

        return true;
    }

    @Override
    public boolean delete(Account account) throws ServiceException {
        if (account == null) {
            return false;
        }

        try {
            List<Phone> phones = account.getPhones();
            if (phones != null && phones.size() > 0) {
                for (Phone p : phones) {
                    phoneDao.delete(p);
                }
            }
            return accountDao.delete(account);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Account getByPk(Integer id) throws ServiceException {
        if (id == null) {
            return null;
        }

        try {
            List<Phone> phones = phoneDao.getPhonesByAccountId(id);
            Account account = accountDao.getByPK(id);
            account.setPhones(phones);

            return account;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Account> getAll() throws ServiceException {
        try {
            List<Account> accounts = accountDao.getAll();
            setAccountsListPhones(accounts);

            return accounts;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private void setAccountsListPhones(List<Account> accounts) throws DaoException {
        if (accounts != null && accounts.size() > 0) {
            for (Account a : accounts) {
                List<Phone> phones = phoneDao.getPhonesByAccountId(a.getId());
                a.setPhones(phones);
            }
        }
    }

    @Transactional
    public void addFriend(Account account, Account friend) throws ServiceException {
        addFriend(account, friend, true);
    }

    private void addFriend(Account account, Account friend, boolean isFirst) throws ServiceException {
        List<Account> friends = account.getFriendList();
        if (friends == null || friends.size() == 0) {
            friends = new ArrayList<>();
        }
        if (friends.contains(friend)) {
            throw new ServiceException("Account id #" + account.getId() + " already has this friend");
        }
        friends.add(friend);
        account.setFriendList(friends);
        update(account);

        if (isFirst) {
            addFriend(friend, account, false);
        }
    }

    @Transactional
    public void removeFriend(Account account, Account friend) throws ServiceException {
        removeFriend(account, friend, true);
    }

    private void removeFriend(Account account, Account friend, boolean isFirst) throws ServiceException {
        List<Account> friends = account.getFriendList();
        if (friends != null && friends.contains(friend)) {
            friends.remove(friend);
            account.setFriendList(friends);
            update(account);
        } else {
            throw new ServiceException("Account id #" + account.getId() + " doesn't have this friend");
        }

        if (isFirst) {
            removeFriend(friend, account, false);
        }
    }

    public List<Account> findByPartName(String findField) throws ServiceException {
        if (findField == null) {
            return null;
        }

        try {
            List<Account> accounts = accountDao.findByPartName(findField);
            setAccountsListPhones(accounts);

            return accounts;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }

    }

    public boolean isPasswordMatch(String email, String password, boolean isEncrypted) throws ServiceException {
        if (email == null || password == null) {
            return false;
        }

        try {
            return accountDao.isPasswordMatch(email, password, isEncrypted);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Account getByEmail(String email) throws ServiceException {
        if (email == null) {
            return null;
        }

        try {
            Account account = accountDao.getByEmail(email);
            List<Phone> phones = phoneDao.getPhonesByAccountId(account.getId());
            account.setPhones(phones);

            return account;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public String getPassword(Account account) throws ServiceException {
        if (account == null) {
            return null;
        }

        try {
            return accountDao.getPassword(account);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public String getEncodedAvatar(Account account) throws ServiceException {
        return Base64.getEncoder().encodeToString(account.getAvatar());
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
        int result = accountDao != null ? accountDao.hashCode() : 0;
        result = 31 * result + (phoneDao != null ? phoneDao.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AccountService{" +
                "accountDao=" + accountDao +
                ", phoneDao=" + phoneDao +
                '}';
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
}