package com.fu.database.dao.impl;

import com.fu.database.dao.SynonymDao;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

/**
 * Created by manlm on 9/21/2016.
 */
@Repository
public class SynonymDaoImpl extends GenericDaoImpl<SynonymDao, Integer> implements SynonymDao {

    private static final Logger LOG = Logger.getLogger(SynonymDaoImpl.class);
}
