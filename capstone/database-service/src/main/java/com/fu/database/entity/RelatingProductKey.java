package com.fu.database.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by manlm on 9/20/2016.
 */
@Embeddable
public class RelatingProductKey implements Serializable {

    @Column(name = "productId1")
    @NotNull
    private long productId1;

    @Column(name = "productId2")
    @NotNull
    private long productId2;

    public long getProductId1() {
        return productId1;
    }

    public void setProductId1(long productId1) {
        this.productId1 = productId1;
    }

    public long getProductId2() {
        return productId2;
    }

    public void setProductId2(long productId2) {
        this.productId2 = productId2;
    }
}
