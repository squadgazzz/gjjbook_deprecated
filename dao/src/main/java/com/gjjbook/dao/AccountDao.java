package com.gjjbook.dao;

import com.gjjbook.domain.Account;
import com.gjjbook.domain.DTO.AccountDTO;
import com.gjjbook.domain.Gender;
import com.gjjbook.domain.Phone;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//@Component
//@Service // Service
//@Controller // Spring MVC
@Repository // DAO
public class AccountDao extends AbstractDao<Account, Integer> {

    public AccountDao() {
    }

    @Override
    public Account update(Account account) throws DaoException {
        String password = account.getPassword();
        password = BCrypt.hashpw(password, BCrypt.gensalt());
        account.setPassword(password);
        if (account.getId() == 0) {
            byte[] avatar = account.getAvatar();
            if (avatar == null) {
                account.setAvatar(getDefaultAvatar(account.getGender()));
            }
            List<Phone> phones = account.getPhones();
            account.setPhones(null);
            account = entityManager.merge(account);
            entityManager.flush();

            if (phones != null && phones.size() > 0) {
                for (Phone p : phones) {
                    account.addPhone(p);
                }
            }
        }

        return entityManager.merge(account);
    }

    @Override
    public Account getByPK(Integer key) {
        if (key == null) {
            return null;
        }

        return entityManager.find(Account.class, key);
    }

    @Override
    public List<Account> getAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> criteriaQuery = criteriaBuilder.createQuery(Account.class);
        Root<Account> from = criteriaQuery.from(Account.class);
        CriteriaQuery<Account> select = criteriaQuery.select(from);
        TypedQuery<Account> typedQuery = entityManager.createQuery(select);

        try {
            return typedQuery.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Account getByEmail(String email) {
        if (email == null) {
            return null;
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> criteriaQuery = criteriaBuilder.createQuery(Account.class);
        Root<Account> from = criteriaQuery.from(Account.class);
        CriteriaQuery<Account> select = criteriaQuery.select(from).where(criteriaBuilder.equal(from.get("email"), email));
        TypedQuery<Account> typedQuery = entityManager.createQuery(select);

        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean isPasswordMatch(String email, String password, boolean isEncrypted) {
        if (email == null || password == null) {
            return false;
        }

        Account account = getByEmail(email);
        if (account != null) {
            String dbPassword = account.getPassword();
            if (dbPassword != null) {
                if (isEncrypted) {
                    return dbPassword.equals(password);
                } else {
                    return BCrypt.checkpw(password, dbPassword);
                }
            }
        }

        return false;
    }

    private byte[] getDefaultAvatar(Gender gender) throws DaoException {
        String fileName;
        if (gender.equals(Gender.MALE)) {
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

    public List<AccountDTO> findByPartName(String query, int currentPage, int pageSize) {
        String[] queryWords = query.split(" ");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AccountDTO> criteriaQuery = cb.createQuery(AccountDTO.class);
        Root<AccountDTO> from = criteriaQuery.from(AccountDTO.class);
        Predicate whereClause = getWhereClause(queryWords, cb, from);

        CriteriaQuery<AccountDTO> select = criteriaQuery.select(from).where(whereClause);
        TypedQuery<AccountDTO> typedQuery = entityManager.createQuery(select);

        return typedQuery.setFirstResult(currentPage * pageSize - pageSize).setMaxResults(pageSize).getResultList();
    }

    public long getSearchResultCount(String query) {
        String[] queryWords = query.split(" ");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<AccountDTO> from = countQuery.from(AccountDTO.class);
        Predicate whereClause = getWhereClause(queryWords, cb, from);

        CriteriaQuery<Long> select = countQuery.select(cb.count(from)).where(whereClause);

        return entityManager.createQuery(select).getSingleResult();
    }

    private Predicate getWhereClause(String[] queryWords, CriteriaBuilder cb, Root<AccountDTO> from) {
        List<Expression<String>> fullName = new ArrayList<>();
        fullName.add(from.get("name"));
        fullName.add(from.get("middleName"));
        fullName.add(from.get("surName"));

        Predicate whereClause = null;
        for (String word : queryWords) {
            Predicate orClause = null;
            for (Expression<String> e : fullName) {
                if (orClause == null) {
                    orClause = cb.or(cb.like(e, "%" + word + "%"));
                } else {
                    orClause = cb.or(orClause, cb.like(e, "%" + word + "%"));
                }
            }

            if (whereClause == null) {
                whereClause = cb.and(orClause);
            } else {
                whereClause = cb.and(whereClause, orClause);
            }
        }

        return whereClause;
    }
}
