package com.fu.database.dao.impl;

import com.fu.database.dao.TrainingPoolDao;
import com.fu.database.entity.TrainingPool;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

/**
 * Created by manlm on 9/21/2016.
 */
@Repository
public class TrainingPoolDaoImpl extends GenericDaoImpl<TrainingPool, Long> implements TrainingPoolDao {

    private static final Logger LOG = Logger.getLogger(TrainingPoolDaoImpl.class);
}
