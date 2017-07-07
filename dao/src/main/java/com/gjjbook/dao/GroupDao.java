package com.gjjbook.dao;

import com.gjjbook.domain.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class GroupDao extends AbstractAutoIncrementIdDao<Group, Integer> {

    public GroupDao(Connection connection) {
        super(connection);
    }

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM Groups";
    }

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO Groups (name, description) \n" +
                "VALUES (?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Groups SET name= ?, description= ?" + getWhereByPKQuery();
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM Groups" + getWhereByPKQuery();
    }

    @Override
    protected List<Group> parseResultSet(ResultSet rs) throws PersistException {
        List<Group> result = new LinkedList<>();
        try {
            while (rs.next()) {
                PersistGroup group = new PersistGroup();
                group.setId(rs.getInt("id"));
                group.setName(rs.getString("name"));
                group.setDescription(rs.getString("description"));
                result.add(group);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Group object) throws PersistException {
        try {
            statement.setString(1, object.getName());
            statement.setString(2, object.getDescription());
        } catch (SQLException e) {
            throw new PersistException(e);
        }
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
    protected void prepareStatementForUpdate(PreparedStatement statement, Group object) throws PersistException {
        try {
            prepareStatementForInsert(statement, object);
            statement.setInt(3, object.getId());
        } catch (SQLException e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForDelete(PreparedStatement statement, Group object) throws SQLException {
        statement.setInt(1, object.getId());
    }

    @Override
    protected void collectForeignData(Group object) {

    }

    @Override
    protected void fillForeignData(Group object, int id) {

    }

    private class PersistGroup extends Group {
        public void setId(int id) {
            super.setId(id);
        }
    }
}
