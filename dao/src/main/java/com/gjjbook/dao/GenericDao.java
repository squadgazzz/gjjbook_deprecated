package com.gjjbook.dao;

import java.util.List;

/**
 * Interface to manage object's persistent state
 *
 * @param <T>  type of persistent object
 * @param <PK> type of primary key
 */

public interface GenericDao<T, PK> {

    T create(T object) throws DaoException;

    T getByPK(PK key) throws DaoException;

    void update(T object) throws DaoException;

    void delete(T object) throws DaoException;

    List<T> getAll() throws DaoException;
}
