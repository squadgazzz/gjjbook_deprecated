package com.gjjbook.dao;

import org.springframework.transaction.annotation.Transactional;

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

    boolean update(T object) throws DaoException;

    boolean delete(T object) throws DaoException;

    List<T> getAll() throws DaoException;
}
