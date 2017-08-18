package com.gjjbook;

import com.gjjbook.dao.DaoException;
import com.gjjbook.dao.GenericDao;
import com.gjjbook.dao.factory.DaoFactory;
import com.gjjbook.domain.Phone;

import java.sql.Connection;
import java.util.List;

public class PhoneService extends AbstractService<Phone, Integer> {

    public PhoneService() throws ServiceException {
        super();
    }

    public PhoneService(DaoFactory<Connection> factory, GenericDao<Phone, Integer> daoObject) {
        super(factory, daoObject);
    }

    @Override
    protected GenericDao<Phone, Integer> getDaoObject() throws ServiceException {
        try {
            return factory.getDao(factory.getContext(), Phone.class);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
