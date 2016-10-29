package com.fu.database.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by manlm on 9/20/2016.
 */
@Embeddable
public class PromotionProductKey implements Serializable {

    @Column(name = "promotionId")
    @NotNull
    private long promotionId;

    @Column(name = "productId")
    @NotNull
    private long productId;

    public PromotionProductKey() {
        // Default Constructor
    }

    /**
     * Constructor
     *
     * @param promotionId
     * @param productId
     */
    public PromotionProductKey(long promotionId, long productId) {
        this.promotionId = promotionId;
        this.productId = productId;
    }

    public long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(long promotionId) {
        this.promotionId = promotionId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
