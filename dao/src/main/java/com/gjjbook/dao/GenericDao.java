package com.gjjbook.dao;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface to manage object's persistent state
 *
 * @param <T>  type of persistent object
 * @param <PK> type of primary key
 */

@Repository
public interface GenericDao<T, PK> {

    T getByPK(PK key) throws DaoException;

    T update(T object) throws DaoException;

    void delete(T object) throws DaoException;

    List<T> getAll() throws DaoException;
}
