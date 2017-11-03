package com.gjjbook.service;

import com.gjjbook.dao.AccountDao;
import com.gjjbook.dao.DaoException;
import com.gjjbook.dao.PhoneDao;
import com.gjjbook.domain.Account;
import com.gjjbook.domain.DTO.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;

@Service // TODO: 25.10.2017 не работает на интерфейсе
@Transactional
// TODO: 25.10.2017 если не ставить аннотацию у всего класса, то не работает. в интерфейсе тоже не работает
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
    public Account update(Account account) throws DaoException {
        if (account == null) {
            return null;
        }

        return accountDao.update(account);
    }

    @Override
    public void delete(Account account) {
        if (account == null) {
            return;
        }

        accountDao.delete(account);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
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

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Account getByEmail(String email) {
        if (email == null) {
            return null;
        }

        return accountDao.getByEmail(email);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean isPasswordMatch(String email, String password, boolean isEncrypted) {
        return !(email == null || password == null) && accountDao.isPasswordMatch(email, password, isEncrypted);
    }

    public String convertByteAvatarToString(byte[] byteArrayAvatar) {
        if (byteArrayAvatar == null) {
            return null;
        }

        return Base64.getEncoder().encodeToString(byteArrayAvatar);
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
}