package com.gjjbook.dao;

import com.gjjbook.domain.Identified;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractJdbcDao<T extends Identified<PK>, PK> implements GenericDao<T, PK> {
    protected Connection connection;

    public AbstractJdbcDao(Connection connection) {
        this.connection = connection;
    }

    protected abstract String getSelectQuery();

    protected abstract String getCreateQuery();

    protected abstract String getUpdateQuery();

    protected abstract String getDeleteQuery();

    protected abstract List<T> parseResultSet(ResultSet rs) throws PersistException;

    protected abstract void prepareStatementForInsert(PreparedStatement statement, T object) throws PersistException;

    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object) throws PersistException;

    protected abstract void prepareStatementForDelete(PreparedStatement statement, T object) throws SQLException;

    @Override
    public T getByPK(PK key) throws PersistException {
        List<T> list;
        String sql = getSelectQuery();
        sql += getWhereByPKQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForGet(statement, key);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }

        if (list == null || list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            throw new PersistException("Received more than one record.");
        }

        T result = list.iterator().next();

        return result;
    }

    protected abstract void prepareStatementForGet(PreparedStatement statement, PK key) throws PersistException;

    @Override
    public List<T> getAll() throws PersistException {
        List<T> list;
        String sql = getSelectQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return list;
    }

    @Override
    public void update(T object) throws PersistException {
        if (object == null) {
            return;
        }

        String sql = getUpdateQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForUpdate(statement, object);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("On update modify more then 1 record: " + count);
            }
            fillForeignData(object, (Integer) object.getPK());
        } catch (Exception e) {
            throw new PersistException(e);
        }

    }

    @Override
    public void delete(T object) throws PersistException {
        if (object == null) {
            return;
        }

        String sql = getDeleteQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForDelete(statement, object);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("On delete modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    protected abstract String getWhereByPKQuery();

    protected abstract void collectForeignData(T object) throws PersistException;

    protected abstract void fillForeignData(T object, int generatedId) throws PersistException;
}
