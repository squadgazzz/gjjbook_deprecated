package com.gjjbook.dao.factory;

import com.gjjbook.dao.GenericDao;
import com.gjjbook.dao.PersistException;

import java.io.Closeable;

/**
 * @param <Context> The entity that describes the session of interaction with the DB
 */
public interface DaoFactory<Context> extends Closeable {

    /**
     * @return connection to database
     * @throws PersistException
     */
    Context getContext() throws PersistException;

    /**
     * @param context
     * @param dtoClass
     * @return object to manage persistent object state
     * @throws PersistException
     */
    GenericDao getDao(Context context, Class dtoClass) throws PersistException;

    interface DAOCreator<Context> {
        GenericDao create(Context context);
    }
}
