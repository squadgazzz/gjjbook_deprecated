package com.gjjbook;

import com.gjjbook.dao.AccountDao;
import com.gjjbook.dao.DaoException;
import com.gjjbook.dao.GenericDao;
import com.gjjbook.dao.factory.DaoFactory;
import com.gjjbook.domain.Account;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class AccountService extends AbstractService<Account, Integer> {

    public AccountService() throws ServiceException {
        super();
    }

    public AccountService(DaoFactory<Connection> factory, GenericDao<Account, Integer> daoObject) {
        super(factory, daoObject);
    }

    @Override
    protected GenericDao<Account, Integer> getDaoObject() throws ServiceException {
        try {
            return factory.getDao(factory.getContext(), Account.class);
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

    public List<Account> findByPartName(String findField) throws DaoException {
        return ((AccountDao) daoObject).findByPartName(findField);
    }

    public void setPassword(Account account, String password) throws ServiceException {
        try {
            ((AccountDao) daoObject).setPassword(account, password);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean isPasswordMatch(String email, String password) throws ServiceException {
        try {
            return ((AccountDao) daoObject).isPasswordMatch(email, password);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Account getByEmail(String email) throws ServiceException {
        try {
            return ((AccountDao) daoObject).getByEmail(email);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void getPassword(Account account) throws ServiceException {
        try {
            ((AccountDao) daoObject).getPassword(account);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }


    public void setAvatar(Account account, byte[] image) throws DaoException {
        ((AccountDao) daoObject).setAvatar(account, image);
    }

    public byte[] getAvatar(Account account) throws DaoException {
        return ((AccountDao) daoObject).getAvatar(account);
    }

    public String getEncodedAvatar(Account account) throws DaoException {
        return Base64.getEncoder().encodeToString(getAvatar(account));
    }


    public static void main(String[] args) throws ServiceException, DaoException, IOException {
        AccountService service = new AccountService();
        Connection connection = service.factory.getContext();

        Path path = Paths.get("C:\\Users\\IZhavoronkov\\Downloads\\if_unknown2_628287.png");
        byte[] image = Files.readAllBytes(path);

        String sql = "INSERT INTO Default_avatars (avatar) VALUES(?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            Blob blob = new SerialBlob(image);
            statement.setBlob(1, blob);
            statement.executeUpdate();
            blob.free();
        } catch (SQLException e) {
            throw new DaoException(e);
        }

       /* AccountService service = new AccountService();
        List<Account> result = service.findByPartName("etr");
        for (Account a : result) {
            System.out.println(a.getName() + " " + a.getMiddleName() + " " + a.getSurName());
        }*/
        /*List<Phone> petrPhones = new LinkedList<>();
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
        service.create(sergey);*/
    }
}