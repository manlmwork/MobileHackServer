package com.fu.database.dao;

import javax.persistence.EntityManager;

/**
 * Created by manlm on 7/23/2016.
 */

/**
 * Generic Dao
 *
 * @param <E> type of entity
 * @param <I> type of id of entity
 */
public interface GenericDao<E, I> {

    /**
     * Insert a record to DB
     *
     * @param entity
     * @return
     */
    E insert(E entity);

    /**
     * Update a record in DB
     *
     * @param entity
     * @return
     */
    E update(E entity);

    /**
     * Delete a record in DB
     *
     * @param entity
     * @return
     */
    E delete(E entity);

    /**
     * Get a record by Id
     *
     * @param id
     * @return
     */
    E getById(I id);

    /**
     *
     * @return
     */
    EntityManager getEntityManager();
}
