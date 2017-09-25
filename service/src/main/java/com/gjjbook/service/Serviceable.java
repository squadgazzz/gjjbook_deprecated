package com.gjjbook.service;

import com.gjjbook.domain.Identified;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface Serviceable<T extends Identified<PK>, PK extends Integer> {

    @Transactional
    T create(T object) throws ServiceException;

    @Transactional
    boolean update(T object) throws ServiceException;

    @Transactional
    boolean delete(T object) throws ServiceException;

    T getByPk(PK id) throws ServiceException;

    List<T> getAll() throws ServiceException;

}
