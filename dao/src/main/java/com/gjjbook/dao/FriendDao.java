package com.gjjbook.dao;

import com.gjjbook.domain.Account;
import com.gjjbook.domain.Friend;
import com.gjjbook.domain.FriendPk;
import com.gjjbook.domain.FriendShipStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * First Account id should be always smaller than the second one.
 */
@Repository
public class FriendDao extends AbstractDao<Friend, FriendPk> {

    public FriendDao() {
    }

    public void requestFriend(Account first, Account second) throws DaoException {
        update(properFriend(first, second, FriendShipStatus.PENDING));
    }

    public void acceptFriend(Account first, Account second) throws DaoException {
        update(properFriend(first, second, FriendShipStatus.ACCEPTED));
    }

    public void declineFriend(Account first, Account second) throws DaoException {
        update(properFriend(first, second, FriendShipStatus.DECLINED));
    }

    public void removeFriend(Account first, Account second) throws DaoException {
        delete(getByPK(new FriendPk(first, second)));
    }

    private Friend properFriend(Account first, Account second, FriendShipStatus status) {
        Friend friend;
        if (first.getId() < second.getId()) {
            friend = new Friend(first, second, status, first);
        } else {
            friend = new Friend(second, first, status, first);
        }

        return friend;
    }

    public String getStatus(Account first, Account second) {
        return getStatus(first.getId(), second.getId());
    }

    public String getStatus(Integer first, Integer second) {
        if (first > second) {
            Integer temp = first;
            first = second;
            second = temp;
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> statusQuery = cb.createQuery(String.class);
        Root<Friend> from = statusQuery.from(Friend.class);

        Predicate whereClause = cb.and(cb.equal(from.get("accountOne").get("id"), first));
        whereClause = cb.and(whereClause, cb.equal(from.get("accountTwo").get("id"), second));

        CriteriaQuery<String> select = statusQuery.select(from.get("status")).where(whereClause);

        try {
            return entityManager.createQuery(select).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Friend getByPK(FriendPk key) throws DaoException {
        return entityManager.find(Friend.class, key);
    }

    @Override
    public List<Friend> getAll() throws DaoException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Friend> criteriaQuery = criteriaBuilder.createQuery(Friend.class);
        Root<Friend> from = criteriaQuery.from(Friend.class);
        CriteriaQuery<Friend> select = criteriaQuery.select(from);
        TypedQuery<Friend> typedQuery = entityManager.createQuery(select);

        try {
            return typedQuery.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Account> getAccountFriends(Account account, int currentPage, int pageSize) {
        Integer id = account.getId();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Friend> statusQuery = cb.createQuery(Friend.class);
        Root<Friend> from = statusQuery.from(Friend.class);

        Predicate whereClause = cb.or(cb.equal(from.get("accountOne").get("id"), id));
        whereClause = cb.or(whereClause, cb.equal(from.get("accountTwo").get("id"), id));
        whereClause = cb.and(whereClause, cb.equal(from.get("status"), FriendShipStatus.ACCEPTED));

        CriteriaQuery<Friend> select = statusQuery.select(from).where(whereClause);
        TypedQuery<Friend> typedQuery = entityManager.createQuery(select);
        List<Friend> friends = typedQuery.setFirstResult(currentPage * pageSize - pageSize).setMaxResults(pageSize).getResultList();

        return getAccountsFromFriends(id, friends);
    }

    public List<Account> getAccountOutRequests(Account account, int currentPage, int pageSize) {
        return getAccountFriendRequests(account, currentPage, pageSize, RequestType.OUT);
    }

    public List<Account> getAccountInRequests(Account account, int currentPage, int pageSize) {
        return getAccountFriendRequests(account, currentPage, pageSize, RequestType.IN);
    }

    private List<Account> getAccountFriendRequests(Account account, int currentPage, int pageSize, RequestType requestType) {
        Integer id = account.getId();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Friend> statusQuery = cb.createQuery(Friend.class);
        Root<Friend> from = statusQuery.from(Friend.class);

        Predicate whereClause = cb.or(cb.equal(from.get("accountOne").get("id"), id));
        whereClause = cb.or(whereClause, cb.equal(from.get("accountTwo").get("id"), id));
        whereClause = cb.and(whereClause, cb.equal(from.get("status"), FriendShipStatus.PENDING));

        Predicate firstSecondPredicate;
        if (requestType == RequestType.IN) {
            firstSecondPredicate = cb.notEqual(from.get("actionAccount").get("id"), id);
        } else {
            firstSecondPredicate = cb.equal(from.get("actionAccount").get("id"), id);
        }

        whereClause = cb.and(whereClause, firstSecondPredicate);

        CriteriaQuery<Friend> select = statusQuery.select(from).where(whereClause);
        TypedQuery<Friend> typedQuery = entityManager.createQuery(select);
        List<Friend> friends = typedQuery.setFirstResult(currentPage * pageSize - pageSize).setMaxResults(pageSize).getResultList();

        return getAccountsFromFriends(id, friends);
    }

    public long getRequestsCount(Account account, RequestType requestType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Friend> from = countQuery.from(Friend.class);
//        Predicate whereClause =

        return 0;
    }

    private enum RequestType {
        CONFIRMED,
        IN,
        OUT
    }

    private List<Account> getAccountsFromFriends(Integer id, List<Friend> friends) {
        List<Account> result = new ArrayList<>();
        for (Friend f : friends) {
            Account fOne = f.getAccountOne();
            if (!fOne.getId().equals(id)) {
                result.add(fOne);
            } else {
                result.add(f.getAccountTwo());
            }
        }

        return result;
    }
}
