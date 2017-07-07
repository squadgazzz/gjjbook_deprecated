package com.gjjbook.dao;

import java.util.List;

/**
 * Interface to manage object's persistent state
 *
 * @param <T>  type of persistent object
 * @param <PK> type of primary key
 */

public interface GenericDao<T, PK> {

    T create(T object) throws PersistException;

    T getByPK(PK key) throws PersistException;

    void update(T object) throws PersistException;

    void delete(T object) throws PersistException;

    List<T> getAll() throws PersistException;
}
