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
import java.util.Arrays;
import java.util.LinkedList;
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
        if (account.getId().equals(0)) {
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

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> criteriaQuery = criteriaBuilder.createQuery(Account.class);
        Root<Account> from = criteriaQuery.from(Account.class);
        CriteriaQuery<Account> select = criteriaQuery.select(from).where(criteriaBuilder.equal(from.get("id"), key));
        TypedQuery<Account> typedQuery = entityManager.createQuery(select);

        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
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
        List<Object[]> queryList = permute(query.split(" "));
//        String sql = "SELECT * FROM accounts WHERE \n" +
//                "REPLACE(CONCAT_WS('', name, middleName, surName), ' ', '') LIKE ? OR\n" + ....

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<AccountDTO> criteriaQuery = cb.createQuery(AccountDTO.class);
        Root<AccountDTO> from = criteriaQuery.from(AccountDTO.class);
        Predicate whereClause = getWhereClause(queryList, cb, from);

        CriteriaQuery<AccountDTO> select = criteriaQuery.select(from).where(whereClause);
        TypedQuery<AccountDTO> typedQuery = entityManager.createQuery(select);

        return typedQuery.setFirstResult(currentPage * pageSize - pageSize).setMaxResults(pageSize).getResultList();
    }

    public long getSearchResultCount(String query) {
        List<Object[]> queryList = permute(query.split(" "));
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<AccountDTO> from = countQuery.from(AccountDTO.class);
        Predicate whereClause = getWhereClause(queryList, cb, from);

        CriteriaQuery<Long> select = countQuery.select(cb.count(from)).where(whereClause);

        return entityManager.createQuery(select).getSingleResult();
    }

    private Predicate getWhereClause(List<Object[]> queryList, CriteriaBuilder cb, Root<AccountDTO> from) {
        Expression<String> fullName = cb.concat(from.get("name"), "");
        fullName = cb.concat(fullName, from.get("middleName"));
        fullName = cb.concat(fullName, from.get("surName"));
        fullName = cb.function("REPLACE", String.class, fullName, cb.literal(" "), cb.literal(""));

        Predicate whereClause = null;
        for (Object[] q : queryList) {
            if (whereClause == null) {
                whereClause = cb.or(cb.like(fullName, "%" + String.join("",
                        Arrays.copyOf(q, q.length, String[].class)) + "%"));
            } else {
                whereClause = cb.or(whereClause, cb.like(fullName, "%" + String.join("",
                        Arrays.copyOf(q, q.length, String[].class)) + "%"));
            }
        }

        return whereClause;
    }

//    private Expression<String> concatExpressions(CriteriaBuilder cb, String delimiter, Expression<String>... expressions) {
//        Expression<String> result = null;
//        for (int i = 0; i < expressions.length; i++) {
//            final boolean first = i == 0, last = i == (expressions.length - 1);
//            final Expression<String> expression = expressions[i];
//            if (first && last) {
//                result = expression;
//            } else if (first) {
//                result = cb.concat(expression, delimiter);
//            } else {
//                result = cb.concat(result, expression);
//                if (!last) {
//                    result = cb.concat(result, delimiter);
//                }
//            }
//        }
//        return result;
//    }

    private List<Object[]> permute(String[] input) {
        List<Object[]> result = new LinkedList<>();
        permute(input, 0, result);

        return result;
    }

    private void permute(String[] input, int k, List<Object[]> result) {
        if (k == input.length) {
            List<String> temp = new LinkedList<>();

            temp.addAll(Arrays.asList(input));
            result.add(temp.toArray());
        } else {
            for (int i = k; i < input.length; i++) {
                String temp = input[k];
                input[k] = input[i];
                input[i] = temp;

                permute(input, k + 1, result);

                temp = input[k];
                input[k] = input[i];
                input[i] = temp;
            }
        }
    }
}
