package com.gjjbook.dao;

import com.gjjbook.domain.Account;
import com.gjjbook.domain.Sex;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

//@Component
//@Service // Service
//@Controller // Spring MVC
@Repository // DAO
public class AccountDao extends AbstractJdbcDao<Account, Integer> {

    public AccountDao() {
    }

    @Autowired
    public AccountDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Account create(Account object) throws DaoException {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("Accounts").usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();

        params.put("name", object.getName());
        params.put("middleName", object.getMiddleName());
        params.put("surName", object.getSurName());
        params.put("sex", object.getSex().name());
        params.put("birthDate", Date.valueOf(object.getBirthDate()));
        params.put("homeAddress", object.getHomeAddress());
        params.put("workAddress", object.getWorkAddress());
        params.put("email", object.getEmail());
        params.put("icq", object.getIcq());
        params.put("skype", object.getSkype());
        params.put("additionalInfo", object.getAdditionalInfo());
        byte[] avatar = getAvatar(object);
        Blob avatarBlob;
        Integer key;
        try {
            avatarBlob = new SerialBlob(avatar);
            params.put("avatar", avatarBlob);

            key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params)).intValue();
            object.setId(key);

            fillExternalData(object);
            avatarBlob.free();
        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return getByPK(key);
    }

    @Override
    public boolean update(Account object) throws DaoException {
        boolean isPasswordOk = true;
        int isUpdateOk = jdbcTemplate.update(getUpdateQuery(), object.getName(), object.getMiddleName(),
                object.getSurName(), object.getSex().name(), object.getBirthDate(), object.getHomeAddress(),
                object.getWorkAddress(), object.getEmail(), object.getIcq(), object.getSkype(),
                object.getAdditionalInfo(), object.getAvatar(), object.getId());

        if (object.getPassword() != null) {
            isPasswordOk = setPassword(object);
        }

        return isPasswordOk && isUpdateOk > 0;
    }

    @Override
    public List<Account> getAll() throws DaoException {
        return jdbcTemplate.query(getSelectQuery(), new BeanPropertyRowMapper<>(Account.class));
    }

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM Accounts";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Accounts SET name= ?, middleName= ?, surName= ?, sex= ?, birthDate= ?, " +
                "homeAddress= ?, workAddress= ?, email= ?, icq= ?, skype= ?, additionalInfo= ?, avatar= ?" +
                getWhereByPKQuery();
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM Accounts" + getWhereByPKQuery();
    }

    @Override
    protected Account parseResultSet(ResultSet rs) throws DaoException {
        Account account = new Account();
        try {
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
            account.setAvatar(convertBlobToBytes(rs.getBlob("avatar")));
            collectExternalData(account);
        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return account;
    }

    private byte[] getAvatar(Account object) throws DaoException {
        byte[] avatar = object.getAvatar();
        if (avatar == null) {
            if (object.getSex().equals(Sex.MALE)) {
                avatar = getDefaultAvatar(Sex.MALE);
            } else {
                avatar = getDefaultAvatar(Sex.FEMALE);
            }
        }
        return avatar;
    }

    private byte[] getDefaultAvatar(Sex sex) throws DaoException {
        String fileName;
        if (sex.equals(Sex.MALE)) {
            fileName = "default-avatar-m.png";
        } else {
            fileName = "default-avatar-f.png";
        }
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            return getBytesArray(is);
        } catch (IOException e) {
            throw new DaoException(e);
        }
    }

    private byte[] getBytesArray(InputStream inputStream) throws IOException {
        byte[] image;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            int next = inputStream.read();
            while (next > -1) {
                bos.write(next);
                next = inputStream.read();
            }
            image = bos.toByteArray();
        }
        return image;
    }


    private byte[] convertBlobToBytes(Blob blob) throws DaoException {
        if (blob == null) {
            return null;
        }

        try {
            return blob.getBytes(1, (int) blob.length());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void fillExternalData(Account object) throws DaoException {
        fillFriends(object);
        setPassword(object);
    }

    private void collectExternalData(Account object) throws DaoException {
        object.setFriendList(getAllFriends(object));
    }

    private List<Account> getAllFriends(Account object) throws DaoException {
        List<Integer> friendsId = getAllFriendsId(object.getId());
        List<Account> result = new ArrayList<>(friendsId.size());
        for (Integer i : friendsId) {
            result.add(getByPK(i));
        }

        return result.size() > 0 ? result : null;
    }

    private List<Integer> getAllFriendsId(int id) {
        String sql = "SELECT * FROM Friends WHERE Account_id=" + id;
        List<Integer> result = new LinkedList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> row : rows) {
            result.add((Integer) row.get("Friend_id"));
        }

        return result;
    }

    private void fillFriends(Account object) throws DaoException {
        List<Account> friends = object.getFriendList();
        int id = object.getId();
        if (friends != null && friends.size() > 0) {
            List<Integer> friendsIdFromDb = getAllFriendsId(id);
            List<Integer> objectFriendsId = new ArrayList<>();
            for (Account f : friends) {
                objectFriendsId.add(f.getId());
                if (!friendsIdFromDb.contains(f.getId())) {
                    String sql = "INSERT INTO Friends Account_id= ?, Friend_id= ?";
                    jdbcTemplate.update(sql, id, f.getId());
                }
            }

            for (Integer friendId : friendsIdFromDb) {
                if (!objectFriendsId.contains(friendId)) {
                    String sql = "DELETE FROM Friends WHERE Account_id= ?, Friend_id= ?";
                    jdbcTemplate.update(sql, id, friendId);
                }
            }
        } else {
            String sql = "DELETE FROM Friends WHERE Account_id= ?";
            jdbcTemplate.update(sql, id);
        }
    }

    public List<Account> findByPartName(String findField) throws DaoException {
        String part = "'%" + findField + "%'";
        String sql = "SELECT * FROM Accounts WHERE name LIKE " + part +
                " OR middlename LIKE " + part +
                " OR surname LIKE " + part;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Account.class));
    }

    private boolean setPassword(Account account) throws DaoException {
        String sql;
        if (getPassword(account) == null) {
            sql = "INSERT INTO Email_password (password, Account_email) VALUES(?,?)";
        } else {
            sql = "UPDATE Email_password SET password= ? WHERE Account_email= ?";
        }

        return jdbcTemplate.update(sql, BCrypt.hashpw(account.getPassword(), BCrypt.gensalt()), account.getEmail()) > 0;
    }

    public String getPassword(Account account) throws DaoException {
        return getPassword(account.getEmail());
    }

    public String getPassword(String email) throws DaoException {
        String sql = "SELECT password FROM Email_password WHERE Account_email= ? ";
        try {
            return jdbcTemplate.queryForObject(sql, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("password");
                }
            }, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean isPasswordMatch(String email, String password, boolean isEncrypted) throws DaoException {
        String dbPassword = getPassword(email);
        if (dbPassword != null) {
            if (isEncrypted) {
                return dbPassword.equals(password);
            } else {
                return BCrypt.checkpw(password, dbPassword);
            }
        } else {
            return false;
        }
    }

    public Account getByEmail(String email) throws DaoException {
        String sql = "SELECT * FROM Accounts WHERE email=?";
        try {
            return this.jdbcTemplate.queryForObject(sql, new RowMapper<Account>() {
                @Override
                public Account mapRow(ResultSet resultSet, int i) throws SQLException {
                    try {
                        return parseResultSet(resultSet);
                    } catch (DaoException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
