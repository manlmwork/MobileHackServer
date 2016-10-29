package com.fu.api.model;

import com.fu.database.model.ProductApi;

import java.util.List;

public class ProductModel {

    private List<ProductApi> productList;

    private long lastSync;

    public List<ProductApi> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductApi> productList) {
        this.productList = productList;
    }

    public long getLastSync() {
        return lastSync;
    }

    public void setLastSync(long lastSync) {
        this.lastSync = lastSync;
    }
}