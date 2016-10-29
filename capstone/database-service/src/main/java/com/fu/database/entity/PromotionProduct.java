package com.fu.database.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by manlm on 9/20/2016.
 */
@Entity
@Table(name = "promotion_product")
public class PromotionProduct {

    @EmbeddedId
    private PromotionProductKey promotionProductKey;

    public PromotionProduct() {
        // Default Constructor
    }

    /**
     * Constructor
     *
     * @param promotionProductKey
     */
    public PromotionProduct(PromotionProductKey promotionProductKey) {
        this.promotionProductKey = promotionProductKey;
    }

    public long getPromotionId() {
        return promotionProductKey.getPromotionId();
    }

    public long getProductId() {
        return promotionProductKey.getProductId();
    }

    public void setPromotionProductKey(long promotionId, long productId) {
        this.promotionProductKey = new PromotionProductKey(promotionId, productId);
    }

    public PromotionProductKey getPromotionProductKey() {
        return promotionProductKey;
    }

    public void setPromotionProductKey(PromotionProductKey promotionProductKey) {
        this.promotionProductKey = promotionProductKey;
    }
}
