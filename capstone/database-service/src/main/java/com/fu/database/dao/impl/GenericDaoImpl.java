package com.fu.database.dao.impl;

import com.fu.database.dao.GenericDao;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;

/**
 * Created by manlm on 7/23/2016.
 */

/**
 * Generic Dao
 *
 * @param <E> type of entity
 * @param <I> type of id of entity
 */
@Service
@Transactional
public abstract class GenericDaoImpl<E, I> implements GenericDao<E, I> {

    private static final Logger LOG = Logger.getLogger(GenericDaoImpl.class);

    private Class<E> entityClass;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Constructor
     */
    @SuppressWarnings(value = "unchecked")
    public GenericDaoImpl() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
    }

    /**
     * Insert a record to DB
     *
     * @param entity
     * @return
     */
    @Override
    public E insert(E entity) {
        LOG.info("[insert] Start: entity = " + entity.getClass().getSimpleName());
        entityManager.persist(entity);
        LOG.info("[insert] End");
        return entity;
    }

    /**
     * Update a record in DB
     *
     * @param entity
     * @return
     */
    @Override
    public E update(E entity) {
        LOG.info("[update] Start: entity = " + entity.getClass().getSimpleName());
        entityManager.merge(entity);
        LOG.info("[update] End");
        return entity;
    }

    /**
     * Delete a record in DB
     *
     * @param entity
     * @return
     */
    @Override
    public E delete(final E entity) {
        LOG.info("[delete] Start: entity = " + entity.getClass().getSimpleName());
        entityManager.remove(entity);
        LOG.info("[delete] End");
        return entity;
    }

    /**
     * Get a record by Id
     *
     * @param id
     * @return
     */
    @Override
    public E getById(final I id) {
        LOG.info("[getById] Start: Id = " + id);
        LOG.info("[getById] End");
        return entityManager.find(entityClass, id);
    }

    /**
     * Get EntityManager to use in child class
     *
     * @return
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
