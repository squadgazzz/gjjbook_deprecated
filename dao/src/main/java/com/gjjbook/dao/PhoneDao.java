package com.gjjbook.dao;

import com.gjjbook.domain.Phone;
import com.gjjbook.domain.PhoneType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PhoneDao extends AbstractJdbcDao<Phone, Integer> {

    public PhoneDao() {
    }

    @Autowired
    public PhoneDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Phone create(Phone phone) throws DaoException {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("Phones").usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();

        params.put("Accounts_id", phone.getOwnerId());
        params.put("type", phone.getType().name());
        params.put("number", phone.getNumber());

        Integer key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params)).intValue();

        return getByPK(key);
    }

    @Override
    public boolean update(Phone phone) throws DaoException {
        return jdbcTemplate.update(getUpdateQuery(), phone.getOwnerId(),
                phone.getType().name(), phone.getNumber(), phone.getId()) > 0;
    }

    @Override
    public List<Phone> getAll() throws DaoException {
        return jdbcTemplate.query(getSelectQuery(), new BeanPropertyRowMapper<>(Phone.class));
    }

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM Phones";
    }

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

    @Override
    protected Phone parseResultSet(ResultSet rs) throws DaoException {
        Phone phone = new Phone();
        try {
            phone.setId(rs.getInt("id"));
            phone.setOwnerId(rs.getInt("Accounts_id"));
            phone.setNumber(rs.getString("number"));
            phone.setType(PhoneType.valueOf(rs.getString("type")));
        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return phone;
    }

    public List<Phone> getPhonesByAccountId(int id) throws DaoException {
        String sql = getSelectQuery() + " WHERE Accounts_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Phone.class), id);
    }
}
