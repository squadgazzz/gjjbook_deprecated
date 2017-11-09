package com.gjjbook.dao;

import com.gjjbook.domain.Phone;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class PhoneDao extends AbstractDao<Phone, Integer> {

    public PhoneDao() {
    }

    @Override
    public Phone getByPK(Integer key) {
        if (key == null) {
            return null;
        }

        return entityManager.find(Phone.class, key);
    }

    @Override
    public List<Phone> getAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Phone> criteriaQuery = criteriaBuilder.createQuery(Phone.class);
        Root<Phone> from = criteriaQuery.from(Phone.class);
        CriteriaQuery<Phone> select = criteriaQuery.select(from);
        TypedQuery<Phone> typedQuery = entityManager.createQuery(select);

        try {
            return typedQuery.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Phone> getPhonesByAccountId(Integer accountId) {
        if (accountId == null) {
            return null;
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Phone> criteriaQuery = criteriaBuilder.createQuery(Phone.class);
        Root<Phone> from = criteriaQuery.from(Phone.class);
        CriteriaQuery<Phone> select = criteriaQuery.select(from).where(criteriaBuilder.equal(from.get("Account_id"), accountId));
        TypedQuery<Phone> typedQuery = entityManager.createQuery(select);

        try {
            return typedQuery.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
