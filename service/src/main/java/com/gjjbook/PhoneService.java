package com.gjjbook;

import com.gjjbook.dao.DaoException;
import com.gjjbook.dao.GenericDao;
import com.gjjbook.dao.connectionPool.ConnectionPool;
import com.gjjbook.dao.factory.DaoFactory;
import com.gjjbook.domain.Phone;

public class PhoneService extends AbstractService<Phone, Integer> {

    public PhoneService() throws ServiceException {
        super();
    }

    public PhoneService(DaoFactory<ConnectionPool> factory, GenericDao<Phone, Integer> daoObject) {
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
