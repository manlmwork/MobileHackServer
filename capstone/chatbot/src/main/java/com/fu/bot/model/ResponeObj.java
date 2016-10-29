package com.fu.bot.model;

import java.util.List;

/**
 * Created by HuyTCM on 10/29/16.
 */
public class ResponeObj {

    private String name = "SERVER";

    private String message;

    private List<ProductObj> productObjList;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ProductObj> getProductObjList() {
        return productObjList;
    }

    public void setProductObjList(List<ProductObj> productObjList) {
        this.productObjList = productObjList;
    }
}
