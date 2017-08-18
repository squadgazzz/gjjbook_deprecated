package com.gjjbook.dao;

import com.gjjbook.domain.Account;
import com.gjjbook.domain.Phone;
import com.gjjbook.domain.Sex;

import javax.sql.rowset.serial.SerialBlob;
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
    protected List<Account> parseResultSet(ResultSet rs) throws DaoException {
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
                account.setIcq(rs.getString("icq"));
                account.setSkype(rs.getString("skype"));
                account.setAdditionalInfo(rs.getString("additionalInfo"));
                collectForeignData(account);
                result.add(account);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Account object) throws DaoException {
        try {
            statement.setString(1, object.getName());
            statement.setString(2, object.getMiddleName());
            statement.setString(3, object.getSurName());
            statement.setString(4, object.getSex().name());
            statement.setObject(5, Date.valueOf(object.getBirthDate()));
            statement.setString(6, object.getHomeAddress());
            statement.setString(7, object.getWorkAddress());
            statement.setString(8, object.getEmail());
            statement.setString(9, object.getIcq());
            statement.setString(10, object.getSkype());
            statement.setString(11, object.getAdditionalInfo());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Account object) throws DaoException {
        try {
            prepareStatementForInsert(statement, object);
            statement.setInt(12, object.getId());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected void prepareStatementForDelete(PreparedStatement statement, Account object) throws SQLException {
        statement.setObject(1, object.getId());
    }

    @Override
    protected void prepareStatementForGet(PreparedStatement statement, Integer key) throws DaoException {
        try {
            statement.setInt(1, key);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected void collectForeignData(Account object) throws DaoException {
        collectFriedns(object);
        collectPhones(object);
    }

    private void collectPhones(Account object) throws DaoException {
        List<Phone> phones = new PhoneDao(connection).getPhonesByAccountId(object.getId());
        object.setPhones(phones);
    }

    private void collectFriedns(Account object) throws DaoException {
        List<Account> friends = getAllFriends(object);
        object.setFriendList(friends);
    }

    @Override
    protected void fillForeignData(Account object, int id) throws DaoException {
        fillFriends(object, id);
        fillPhones(object, id);
    }

    private void fillFriends(Account object, int id) throws DaoException {
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
                        throw new DaoException(e);
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
                        throw new DaoException(e);
                    }
                }
            }
        } else {
            String sql = "DELETE FROM Friends WHERE Account_id= ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        }
    }

    private void fillPhones(Account object, int id) throws DaoException {
        List<Phone> phones = object.getPhones();
        PhoneDao phoneDao = new PhoneDao(connection);
        List<Phone> phonesFromDb = phoneDao.getPhonesByAccountId(id);
        if (phones != null) {
            if (phones.size() > 0) {
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
    }

    private List<Integer> getAllFriendsId(int id) throws DaoException {
        List<Integer> result = new ArrayList<>();
        String sql = "SELECT * FROM Friends WHERE Account_id=" + id;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                result.add(rs.getInt("Friend_id"));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

    private List<Account> getAllFriends(Account object) throws DaoException {
        List<Integer> friendsId = getAllFriendsId(object.getId());
        List<Account> result = new ArrayList<>(friendsId.size());
        for (Integer i : friendsId) {
            result.add(getByPK(i));
        }
        return result;
    }

    public List<Account> findByPartName(String findField) throws DaoException {
        List<Account> result;
        String part = "'%" + findField + "%'";
        String sql = "SELECT * FROM Accounts WHERE name LIKE " + part +
                " OR middlename LIKE " + part +
                " OR surname LIKE " + part;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            result = parseResultSet(statement.executeQuery());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

    public void setPassword(Account account, String password) throws DaoException {
        String sql;
        if (getPassword(account) == null) {
            sql = "INSERT INTO Email_password (password, Account_email) VALUES(?,?)";
        } else {
            sql = "UPDATE Email_password SET password= ? WHERE Account_email= ?";
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, password);
            statement.setString(2, account.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public String getPassword(Account account) throws DaoException {
        String sql = "SELECT password FROM Email_password WHERE Account_email= ? ";
        String result = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, account.getEmail());
            ResultSet rs = statement.executeQuery();
            if (rs == null) {
                return null;
            }

            if (rs.next()) {
                result = rs.getString("password");
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return result;
    }

    public boolean isPasswordMatch(String email, String password) throws DaoException {
        String sql = "SELECT COUNT(*) as MATCHES FROM Email_password WHERE Account_email=? AND password=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();

            return rs.next() && rs.getInt("MATCHES") != 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Account getByEmail(String email) throws DaoException {
        List<Account> result;
        String sql = "SELECT * FROM Accounts WHERE email=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            result = parseResultSet(rs);
            if (result.size() > 1) {
                throw new DaoException("Found more than 1 account with email: " + email);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result.get(0);
    }

    public void setAvatar(Account account, byte[] image) throws DaoException {
        String sql;
        if (isAccountAvatar(account)) {
            sql = "UPDATE Account_avatar SET Avatar=? WHERE Account_id=?";
        } else {
            sql = "INSERT INTO Account_avatar (Avatar, Account_id) VALUES(?,?)";
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            Blob blob = new SerialBlob(image);
            statement.setBlob(1, blob);
            statement.setInt(2, account.getId());
            statement.executeUpdate();
            blob.free();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private boolean isAccountAvatar(Account account) throws DaoException {
        String sql = "SELECT COUNT(*) AS MATCHES FROM Account_avatar WHERE Account_id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, account.getId());
            ResultSet rs = statement.executeQuery();

            return rs.next() && rs.getInt("MATCHES") != 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public byte[] getAvatar(Account account) throws DaoException {
        byte[] result;
        String sql = "SELECT avatar FROM Account_avatar WHERE Account_id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, account.getId());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Blob blob = rs.getBlob("avatar");
                result = blob.getBytes(1, (int) blob.length());
                blob.free();
            } else {
                result = getDefaultAvatar(1);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return result;
    }

    private byte[] getDefaultAvatar(int id) throws DaoException {
        byte[] result = null;
        String sql = "SELECT avatar FROM Default_avatars WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Blob blob = rs.getBlob("avatar");
                result = blob.getBytes(1, (int) blob.length());
                blob.free();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return result;
    }

    private class PersistAccount extends Account {
        public void setId(int id) {
            super.setId(id);
        }
    }
}
