package com.fu.database.dao.impl;

import com.fu.database.dao.AreaDao;
import com.fu.database.entity.Area;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by manlm on 9/21/2016.
 */
@Repository
public class AreaDaoImpl extends GenericDaoImpl<Area, Integer> implements AreaDao {

    private static final Logger LOG = Logger.getLogger(AreaDaoImpl.class);

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Area> getAll() {
        LOG.info("[getAll] Start");
        Query query = getEntityManager().createQuery("FROM " + Area.class.getSimpleName());
        LOG.info("[getAll] End");
        return query.getResultList();
    }
}
