package com.fu.database.dao.impl;

import com.fu.database.dao.WeightedHistoryDao;
import com.fu.database.entity.Product;
import com.fu.database.entity.WeightedHistory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by manlm on 9/23/2016.
 */
@Repository
public class WeightedHistoryDaoImpl extends GenericDaoImpl<WeightedHistory, Long> {

    private static final Logger LOG = Logger.getLogger(WeightedHistoryDaoImpl.class);


}
