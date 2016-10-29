package com.fu.api.model;

import java.util.List;

/**
 * Created by manlm on 10/2/2016.
 */

public class CartInfo {

    private String botFbId;

    private List<Cart> cart;

    public String getBotFbId() {
        return botFbId;
    }

    public void setBotFbId(String botFbId) {
        this.botFbId = botFbId;
    }

    public List<Cart> getCart() {
        return cart;
    }

    public void setCart(List<Cart> cart) {
        this.cart = cart;
    }
}
