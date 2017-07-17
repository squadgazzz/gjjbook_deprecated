package com.gjjbook.dao.factory;

import com.gjjbook.dao.GenericDao;
import com.gjjbook.dao.DaoException;

import java.io.Closeable;

/**
 * @param <Context> The entity that describes the session of interaction with the DB
 */
public interface DaoFactory<Context> extends Closeable {

    /**
     * @return connection to database
     * @throws DaoException
     */
    Context getContext() throws DaoException;

    /**
     * @param context
     * @param dtoClass
     * @return object to manage persistent object state
     * @throws DaoException
     */
    GenericDao getDao(Context context, Class dtoClass) throws DaoException;

    interface DAOCreator<Context> {
        GenericDao create(Context context);
    }
}
