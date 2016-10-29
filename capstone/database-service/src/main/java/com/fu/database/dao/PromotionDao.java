package com.fu.database.dao;

import com.fu.database.entity.Promotion;
import com.fu.database.entity.PromotionProductKey;

import java.util.List;

/**
 * Created by manlm on 9/21/2016.
 */
public interface PromotionDao extends GenericDao<Promotion, Long> {

    List<Promotion> getPromotionByProductId(long productId);

    public List<Promotion> getSpecificPromotionByProductId(long productId);
}
