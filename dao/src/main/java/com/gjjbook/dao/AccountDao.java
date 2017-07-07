package com.gjjbook.dao;

import com.gjjbook.domain.Account;
import com.gjjbook.domain.Phone;
import com.gjjbook.domain.Sex;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AccountDao extends AbstractAutoIncrementIdDao<Account, Integer> {

    public AccountDao(Connection connection) {
        super(connection);
    }

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM Accounts";
    }

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO Accounts (name, middleName, surName, sex, birthDate, " +
                "homeAddress, workAddress, email, icq, skype, additionalInfo)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Accounts SET name= ?, middleName= ?, surName= ?, sex= ?, birthDate= ?, " +
                "homeAddress= ?, workAddress= ?, email= ?, icq= ?, skype= ?, additionalInfo= ?" +
                getWhereByPKQuery();
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM Accounts" + getWhereByPKQuery();
    }

    @Override
    protected List<Account> parseResultSet(ResultSet rs) throws PersistException {
        List<Account> result = new LinkedList<>();
        try {
            while (rs.next()) {
                PersistAccount account = new PersistAccount();
                account.setId(rs.getInt("id"));
                account.setName(rs.getString("name"));
                account.setMiddleName(rs.getString("middleName"));
                account.setSurName(rs.getString("surName"));
                account.setSex(Sex.valueOf(rs.getString("sex")));
                account.setBirthDate(((Date) rs.getObject("birthDate")).toLocalDate());
                account.setHomeAddress(rs.getString("homeAddress"));
                account.setWorkAddress(rs.getString("workAddress"));
                account.setEmail(rs.getString("email"));
                account.setIcq(rs.getInt("icq"));
                account.setSkype(rs.getString("skype"));
                account.setAdditionalInfo(rs.getString("additionalInfo"));
                collectForeignData(account);
                result.add(account);
            }
        } catch (SQLException e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Account object) throws PersistException {
        try {
            statement.setString(1, object.getName());
            statement.setString(2, object.getMiddleName());
            statement.setString(3, object.getSurName());
            statement.setString(4, object.getSex().name());
            statement.setObject(5, Date.valueOf(object.getBirthDate()));
            statement.setString(6, object.getHomeAddress());
            statement.setString(7, object.getWorkAddress());
            statement.setString(8, object.getEmail());
            statement.setInt(9, object.getIcq());
            statement.setString(10, object.getSkype());
            statement.setString(11, object.getAdditionalInfo());
        } catch (SQLException e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Account object) throws PersistException {
        try {
            prepareStatementForInsert(statement, object);
            statement.setInt(12, object.getId());
        } catch (SQLException e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForDelete(PreparedStatement statement, Account object) throws SQLException {
        statement.setObject(1, object.getId());
    }

    @Override
    protected void prepareStatementForGet(PreparedStatement statement, Integer key) throws PersistException {
        try {
            statement.setInt(1, key);
        } catch (SQLException e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void collectForeignData(Account object) throws PersistException {
        collectFriedns(object);
        collectPhones(object);
    }

    private void collectPhones(Account object) throws PersistException {
        List<Phone> phones = new PhoneDao(connection).getPhonesByAccountId(object.getId());
        object.setPhones(phones);
    }

    private void collectFriedns(Account object) throws PersistException {
        List<Account> friends = getAllFriends(object);
        object.setFriendList(friends);
    }

    @Override
    protected void fillForeignData(Account object, int id) throws PersistException {
        fillFriends(object, id);
        fillPhones(object, id);
    }

    private void fillFriends(Account object, int id) throws PersistException {
        List<Account> friends = object.getFriendList();
        if (friends != null && friends.size() > 0) {
            List<Integer> friendsIdFromDb = getAllFriendsId(id);
            List<Integer> objectFriendsId = new ArrayList<>();
            for (Account f : friends) {
                objectFriendsId.add(f.getId());
                if (!friendsIdFromDb.contains(f.getId())) {
                    String sql = "INSERT INTO Friends Account_id= ?, Friend_id= ?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setInt(1, id);
                        statement.setInt(2, f.getId());
                        statement.executeUpdate();
                    } catch (SQLException e) {
                        throw new PersistException(e);
                    }
                }
            }

            for (Integer friendId : friendsIdFromDb) {
                if (!objectFriendsId.contains(friendId)) {
                    String sql = "DELETE FROM Friends WHERE Account_id= ?, Friend_id= ?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setInt(1, id);
                        statement.setInt(2, friendId);
                        statement.executeUpdate();
                    } catch (SQLException e) {
                        throw new PersistException(e);
                    }
                }
            }
        } else {
            String sql = "DELETE FROM Friends WHERE Account_id= ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new PersistException(e);
            }
        }
    }

    private void fillPhones(Account object, int id) throws PersistException {
        List<Phone> phones = object.getPhones();
        List<Phone> phonesFromDb = new PhoneDao(connection).getPhonesByAccountId(id);
        GenericDao<Phone, Integer> phoneDao = new PhoneDao(connection);
        if (phones != null && phones.size() > 0) {
            for (Phone p : phones) {
                p.setOwnerId(id);
                if (phonesFromDb == null || !phonesFromDb.contains(p)) {
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

    private List<Integer> getAllFriendsId(int id) throws PersistException {
        List<Integer> result = new ArrayList<>();
        String sql = "SELECT * FROM Friends WHERE Account_id=" + id;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                result.add(rs.getInt("Friend_id"));
            }
        } catch (SQLException e) {
            throw new PersistException(e);
        }
        return result;
    }

    private List<Account> getAllFriends(Account object) throws PersistException {
        List<Integer> friendsId = getAllFriendsId(object.getId());
        List<Account> result = new ArrayList<>(friendsId.size());
        for (Integer i : friendsId) {
            result.add(getByPK(i));
        }
        return result;
    }

    private class PersistAccount extends Account {
        public void setId(int id) {
            super.setId(id);
        }
    }
}
