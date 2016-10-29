package com.fu.bot.model;

import java.io.Serializable;

public class SaveData implements Serializable{

    long productId;
    long timeHandle;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getTimeHandle() {
        return timeHandle;
    }

    public void setTimeHandle(long timeHandle) {
        this.timeHandle = timeHandle;
    }

    public SaveData(long productId, long timeHandle) {
        this.productId = productId;
        this.timeHandle = timeHandle;
    }

}
