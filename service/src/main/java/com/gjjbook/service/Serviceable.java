package com.gjjbook.service;

import com.gjjbook.dao.DaoException;
import com.gjjbook.domain.Identified;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface Serviceable<T extends Identified<PK>, PK extends Integer> {

    T update(T object) throws ServiceException, DaoException;

    void delete(T object) throws ServiceException;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    T getByPk(PK id) throws ServiceException;

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    List<T> getAll() throws ServiceException;

}
