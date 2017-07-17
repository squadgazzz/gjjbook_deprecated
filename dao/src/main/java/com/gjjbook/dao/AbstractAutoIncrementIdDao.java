package com.gjjbook.dao;

import com.gjjbook.domain.Identified;

import java.sql.*;
import java.util.List;

public abstract class AbstractAutoIncrementIdDao<T extends Identified<PK>, PK extends Integer> extends AbstractJdbcDao<T, PK> {

    public AbstractAutoIncrementIdDao(Connection connection) {
        super(connection);
    }

    @Override
    public T create(T object) throws DaoException {
        if (object == null) {
            return null;
        }

        T persistInstance;
        String sql = getCreateQuery();
        PK generatedId;
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatementForInsert(statement, object);
            int count = statement.executeUpdate();
            generatedId = getGeneratedId(statement);
            if (count != 1) {
                throw new DaoException("On create modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }

        fillForeignData(object, generatedId);

        sql = getSelectQuery() + getWhereByPKQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForGet(statement, generatedId);
            ResultSet rs = statement.executeQuery();
            List<T> list = parseResultSet(rs);
            if (list == null || list.size() != 1) {
                throw new DaoException("Exception on findByPK new create data. List size=" + list.size());
            }
            persistInstance = list.iterator().next();
        } catch (Exception e) {
            throw new DaoException(e);
        }

        return persistInstance;
    }

    protected PK getGeneratedId(PreparedStatement statement) throws DaoException {
        try (ResultSet rs = statement.getGeneratedKeys()) {
            if (rs.next()) {
                return (PK) new Integer(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return null;
    }

    @Override
    protected String getWhereByPKQuery() {
        return " WHERE id = ?";
    }
}
