package com.fu.api.model;

/**
 * Created by manlm on 10/2/2016.
 */

public class Cart {

    private int productId;

    private int isFound;

    private long lastUpdate;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getIsFound() {
        return isFound;
    }

    public void setIsFound(int isFound) {
        this.isFound = isFound;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
