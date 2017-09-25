package com.gjjbook.dao;

import com.gjjbook.domain.Identified;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractJdbcDao<T extends Identified<PK>, PK> implements GenericDao<T, PK> {
    protected JdbcTemplate jdbcTemplate;

    public AbstractJdbcDao() {
    }

    public AbstractJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected abstract String getSelectQuery();

    protected abstract String getUpdateQuery();

    protected abstract String getDeleteQuery();

    protected abstract T parseResultSet(ResultSet rs) throws DaoException;

    @Override
    public T getByPK(PK key) throws DaoException {
        try {
            return jdbcTemplate.queryForObject(getSelectQuery() + getWhereByPKQuery(), new RowMapper<T>() {
                @Override
                public T mapRow(ResultSet resultSet, int i) throws SQLException {
                    try {
                        return parseResultSet(resultSet);
                    } catch (DaoException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }, key);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean delete(T object) throws DaoException {
        return jdbcTemplate.update(getDeleteQuery(), object.getPK()) > 0;
    }

    protected String getWhereByPKQuery() {
        return " WHERE id = ?";
    }

    @Override
    public String toString() {
        return "AbstractJdbcDao{" +
                "jdbcTemplate=" + jdbcTemplate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractJdbcDao<?, ?> that = (AbstractJdbcDao<?, ?>) o;

        return jdbcTemplate != null ? jdbcTemplate.equals(that.jdbcTemplate) : that.jdbcTemplate == null;
    }

    @Override
    public int hashCode() {
        return jdbcTemplate != null ? jdbcTemplate.hashCode() : 0;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
