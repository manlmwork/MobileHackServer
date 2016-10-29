package com.fu.database.dao.impl;

import com.fu.database.dao.RelatingProductDao;
import com.fu.database.entity.RelatingProduct;
import com.fu.database.entity.RelatingProductKey;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

/**
 * Created by manlm on 9/21/2016.
 */
@Repository
public class RelatingProductDaoImpl extends GenericDaoImpl<RelatingProduct, RelatingProductKey> implements RelatingProductDao {

    private static final Logger LOG = Logger.getLogger(RelatingProductDaoImpl.class);
}
