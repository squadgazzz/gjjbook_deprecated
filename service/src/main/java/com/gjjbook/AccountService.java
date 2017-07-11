package com.gjjbook;

import com.gjjbook.dao.GenericDao;
import com.gjjbook.dao.PersistException;
import com.gjjbook.dao.factory.DaoFactory;
import com.gjjbook.domain.Account;
import com.gjjbook.domain.Identified;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AccountService extends AbstractService<Account, Integer> {

    public AccountService(DaoFactory<Connection> factory, GenericDao<Account, Integer> daoObject) {
        super(factory, daoObject);
    }

    public Account create(Account account) throws PersistException {
        if (account == null) {
            return null;
        }

        return daoObject.create(account);
    }

    public void update(Account account) throws PersistException {
        if (account == null) {
            return;
        }

        daoObject.update(account);
    }

    public void delete(Account account) throws PersistException {
        if (account == null) {
            return;
        }

        daoObject.delete(account);
    }

    public Account getByPk(Integer id) throws PersistException {
        if (id == null) {
            return null;
        }

        return daoObject.getByPK(id);
    }

    public List<Account> getAll() throws PersistException {
        return daoObject.getAll();
    }

    public void addFriend(Account account, Account friend) throws PersistException {
        addFriend(account, friend, true);
    }

    private void addFriend(Account account, Account friend, boolean isFirst) throws PersistException {
        List<Account> friends = account.getFriendList();
        if (friends == null || friends.size() == 0) {
            friends = new ArrayList<>();
        }
        if (friends.contains(friend)) {
            throw new PersistException("Account id #" + account.getId() + " already has this friend");
        }
        friends.add(friend);
        account.setFriendList(friends);
        update(account);

        if (isFirst) {
            addFriend(friend, account, false);
        }
    }

    public void removeFriend(Account account, Account friend) throws PersistException {
        removeFriend(account, friend, true);
    }

    private void removeFriend(Account account, Account friend, boolean isFirst) throws PersistException {
        List<Account> friends = account.getFriendList();
        if (friends != null && friends.contains(friend)) {
            friends.remove(friend);
            account.setFriendList(friends);
            update(account);
        } else {
            throw new PersistException("Account id #" + account.getId() + " doesn't have this friend");
        }

        if (isFirst) {
            removeFriend(friend, account, false);
        }
    }

    public List<Account> getFriends(Account account) {
        return account.getFriendList();
    }
}
