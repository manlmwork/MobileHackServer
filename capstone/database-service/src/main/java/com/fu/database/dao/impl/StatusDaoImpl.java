package com.fu.database.dao.impl;

import com.fu.database.dao.StatusDao;
import com.fu.database.entity.Status;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

/**
 * Created by manlm on 9/21/2016.
 */
@Repository
public class StatusDaoImpl extends GenericDaoImpl<Status, Integer> implements StatusDao {

    private static final Logger LOG = Logger.getLogger(StatusDaoImpl.class);
}
