package com.fu.database.dao.impl;

import com.fu.database.dao.PromotionProductDao;
import com.fu.database.entity.PromotionProduct;
import com.fu.database.entity.PromotionProductKey;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

/**
 * Created by manlm on 9/21/2016.
 */
@Repository
public class PromotionProductDaoImpl extends GenericDaoImpl<PromotionProduct, PromotionProductKey> implements PromotionProductDao {

    private static final Logger LOG = Logger.getLogger(PromotionProductDaoImpl.class);
}
