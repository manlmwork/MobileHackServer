package com.fu.database.dao.impl;

import com.fu.common.constant.KeyConstant;
import com.fu.database.dao.PromotionDao;
import com.fu.database.entity.Promotion;
import com.fu.database.entity.PromotionProduct;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by manlm on 9/21/2016.
 */
@Repository
public class PromotionDaoImpl extends GenericDaoImpl<Promotion, Long> implements PromotionDao {

    private static final Logger LOG = Logger.getLogger(PromotionDaoImpl.class);

    @Override
    public List<Promotion> getPromotionByProductId(long productId) {
        LOG.info("[getPromotionByProductId] Start: productId = " + productId);
        LOG.info("[getPromotionByProductId] End");
        return getEntityManager().createQuery(String.valueOf(new StringBuilder("SELECT p FROM ")
                .append(Promotion.class.getSimpleName())
                .append(" p ")
                .append("WHERE p.id IN (SELECT pp.promotionProductKey.promotionId FROM ")
                .append(PromotionProduct.class.getSimpleName())
                .append(" pp WHERE pp.promotionProductKey.productId = :productId OR pp.promotionProductKey.productId = :productPromotionId)")))
                .setParameter("productPromotionId", KeyConstant.PRODUCT_PROMOTION_ID)
                .setParameter("productId", productId)
                .getResultList();
    }

    @Override
    public List<Promotion> getSpecificPromotionByProductId(long productId) {
        LOG.info("[getPromotionByProductId] Start: productId = " + productId);
        LOG.info("[getPromotionByProductId] End");
        return getEntityManager().createQuery(String.valueOf(new StringBuilder("SELECT p FROM ")
                .append(Promotion.class.getSimpleName())
                .append(" p ")
                .append("WHERE  p.id  IN (SELECT pp.promotionProductKey.promotionId FROM ")
                .append(PromotionProduct.class.getSimpleName())
                .append(" pp WHERE pp.promotionProductKey.productId = :productId)")))
                .setParameter("productId", productId)
                .getResultList();
    }
}
