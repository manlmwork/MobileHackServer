package com.fu.api.service;

import com.fu.api.model.*;
import com.fu.bot.model.ChatMessage;
import com.fu.bot.model.ProductResponse;
import com.fu.bot.model.SaveData;
import com.fu.database.entity.Area;
import com.fu.database.model.BeaconApi;
import com.fu.database.model.ProductApi;

import java.util.List;

/**
 * Created by manlm on 10/1/2016.
 */
public interface IsmbRestService {

    boolean authenticate(String token);

    String registerPhone(String deviceToken, PhoneInfo phoneInfo);

    List<SaveData> getCart(String botFbId);

    void syncCart(CartInfo cartInfo);

    ProductModel getProduct();

    BeaconModel getBeacon();

    AreaModel getArea();

    String saveImg(ChatMessage chatMessage);
}
