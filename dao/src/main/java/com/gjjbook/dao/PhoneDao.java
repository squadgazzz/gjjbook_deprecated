package com.gjjbook.dao;

import com.gjjbook.domain.Phone;
import com.gjjbook.domain.PhoneType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PhoneDao extends AbstractAutoIncrementIdDao<Phone, Integer> {

    public PhoneDao(Connection connection) {
        super(connection);
    }

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM Phones";
    }

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO Phones (Accounts_id, type, number) " +
                "VALUES (?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Phones SET Accounts_id= ?, type= ?, number= ? " +
                getWhereByPKQuery();
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM Phones" + getWhereByPKQuery();
    }

    public List<Phone> getPhonesByAccountId(int id) throws DaoException {
        List<Phone> list;
        String sql = getSelectQuery();
        sql += " WHERE Accounts_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new DaoException(e);
        }

        if (list == null || list.size() == 0) {
            return null;
        }

        return list;
    }

    @Override
    protected List<Phone> parseResultSet(ResultSet rs) throws DaoException {
        List<Phone> result = new LinkedList<>();
        try {
            while (rs.next()) {
                PersistPhone phone = new PersistPhone();
                phone.setId(rs.getInt("id"));
                phone.setOwnerId(rs.getInt("Accounts_id"));
                phone.setNumber(rs.getString("number"));
                phone.setType(PhoneType.valueOf(rs.getString("type")));
                result.add(phone);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Phone object) throws DaoException {
        try {
            statement.setInt(1, object.getOwnerId());
            statement.setString(2, object.getType().name());
            statement.setString(3, object.getNumber());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Phone object) throws DaoException {
        try {
            prepareStatementForInsert(statement, object);
            statement.setInt(5, object.getId());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected void prepareStatementForDelete(PreparedStatement statement, Phone object) throws SQLException {
        statement.setInt(1, object.getId());
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
    protected void collectForeignData(Phone object) {
// nothing to collect yet
    }

    @Override
    protected void fillForeignData(Phone object, int id) {
// nothing to collect yet
    }

    private class PersistPhone extends Phone {
        public void setId(int id) {
            super.setId(id);
        }
    }
}
