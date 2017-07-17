package com.gjjbook;

import com.gjjbook.dao.GenericDao;
import com.gjjbook.dao.DaoException;
import com.gjjbook.dao.factory.DaoFactory;
import com.gjjbook.domain.Account;
import com.gjjbook.domain.Phone;
import com.gjjbook.domain.PhoneType;
import com.gjjbook.domain.Sex;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AccountService extends AbstractService<Account, Integer> {

    public AccountService() throws ServiceException {
        super();
    }

    public AccountService(DaoFactory<Connection> factory, GenericDao<Account, Integer> daoObject) {
        super(factory, daoObject);
    }

    public Account create(Account account) throws ServiceException {
        if (account == null) {
            return null;
        }

        try {
            return daoObject.create(account);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void update(Account account) throws ServiceException {
        if (account == null) {
            return;
        }

        try {
            daoObject.update(account);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void delete(Account account) throws ServiceException {
        if (account == null) {
            return;
        }

        try {
            daoObject.delete(account);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Account getByPk(Integer id) throws ServiceException {
        if (id == null) {
            return null;
        }

        try {
            return daoObject.getByPK(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Account> getAll() throws ServiceException {
        try {
            return daoObject.getAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

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

    public List<Account> getFriends(Account account) {
        return account.getFriendList();
    }

    public static void main(String[] args) throws ServiceException {
        List<Phone> petrPhones = new LinkedList<>();
        petrPhones.add(new Phone(PhoneType.MOBILE, 7, 9649998877L));
        petrPhones.add(new Phone(PhoneType.WORK, 7, 4958887766L));

        List<Phone> ivanPhones = new LinkedList<>();
        ivanPhones.add(new Phone(PhoneType.MOBILE, 7, 9641112233L));
        ivanPhones.add(new Phone(PhoneType.HOME, 7, 4958887755L));
        ivanPhones.add(new Phone(PhoneType.WORK, 7, 4957776655L));

        List<Phone> sergeyPhones = new LinkedList<>();
        sergeyPhones.add(new Phone(PhoneType.MOBILE, 7, 9642223355L));


        Account petr = new Account("Petr", null, "Ivanov", Sex.MALE, LocalDate.parse("2016-08-16"),
                petrPhones, "home", "work", "1", 1234, "skype",
                "no add info", null, null);
        Account ivan = new Account("Petr", null, "Ivanov", Sex.MALE, LocalDate.parse("2016-08-16"),
                ivanPhones, "home", "work", "2", 1234, "skype",
                "no add info", null, null);
        Account sergey = new Account("Petr", null, "Ivanov", Sex.MALE, LocalDate.parse("2016-08-16"),
                sergeyPhones, "home", "work", "3", 1234, "skype",
                "no add info", null, null);

        AccountService service = new AccountService();
        service.create(petr);
        service.create(ivan);
        service.create(sergey);
    }
}
