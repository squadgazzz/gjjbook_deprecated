package com.gjjbook.dao;

import com.gjjbook.domain.Identified;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AbstractDao<T extends Identified<PK>, PK> implements GenericDao<T, PK> {

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public T update(T object) throws DaoException {
        if (object == null) {
            return null;
        }

        return entityManager.merge(object);
    }

    @Override
    public void delete(T object) {
        if (object == null) {
            return;
        }

        entityManager.remove(object);
    }
}
