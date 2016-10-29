package com.fu.api.controller;

import com.fu.api.model.BotInfo;
import com.fu.api.model.CartInfo;
import com.fu.api.model.PhoneInfo;
import com.fu.api.service.IsmbRestService;
import com.fu.bot.model.ChatMessage;
import com.fu.bot.model.SaveData;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manlm on 10/1/2016.
 */
@RestController
public class IsmbRestController {

    private static final Logger LOG = Logger.getLogger(IsmbRestController.class);

    private final IsmbRestService ismbRestService;

    @Autowired
    public IsmbRestController(IsmbRestService ismbRestService) {
        this.ismbRestService = ismbRestService;
    }

    @RequestMapping(value = "/registerPhone", method = RequestMethod.POST)
    public ResponseEntity<String> registerPhone(@RequestHeader(value = "Device-Token") String deviceToken,
                                                @RequestBody PhoneInfo phoneInfo) {
        LOG.info("[registerPhone] Start");
        LOG.info("[registerPhone] End");
        return new ResponseEntity<>(ismbRestService.registerPhone(deviceToken, phoneInfo), HttpStatus.OK);
    }

    @RequestMapping(value = "/getCart", method = RequestMethod.POST)
    public ResponseEntity<String> getCart(@RequestHeader(value = "Device-Token") String deviceToken,
                                          @RequestBody BotInfo botInfo) {
        LOG.info("[getCart] Start");

        if (ismbRestService.authenticate(deviceToken)) {
            List<SaveData> productIdList = (List<SaveData>) ismbRestService.getCart(botInfo.getBotFbId());
            LOG.info("[getCart] End");
            return new ResponseEntity<>(new Gson().toJson(productIdList), HttpStatus.OK);
        }

        LOG.info("[getCart] End");
        return new ResponseEntity<>(new Gson().toJson(new ArrayList<>()), HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/syncCart", method = RequestMethod.POST)
    public ResponseEntity<String> syncCart(@RequestHeader(value = "Device-Token") String deviceToken,
                                           @RequestBody CartInfo cartInfo) {
        LOG.info("[syncCart] Start");

        if (ismbRestService.authenticate(deviceToken)) {
            ismbRestService.syncCart(cartInfo);

            LOG.info("[syncCart] End");
            return new ResponseEntity<>(new Gson().toJson(HttpStatus.OK.value()), HttpStatus.OK);
        }

        LOG.info("[syncCart] End");
        return new ResponseEntity<>(new Gson().toJson(HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/getProduct", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> getProduct(@RequestHeader(value = "Device-Token") String deviceToken) {
        LOG.info("[getProduct] Start");

        if (ismbRestService.authenticate(deviceToken)) {

            LOG.info("[getProduct] End");
            return new ResponseEntity<>(new Gson().toJson(ismbRestService.getProduct()), HttpStatus.OK);
        }

        LOG.info("[getProduct] End");
        return new ResponseEntity<>(new Gson().toJson(new ArrayList<>()), HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/getBeacon", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> getBeacon(@RequestHeader(value = "Device-Token") String deviceToken) {
        LOG.info("[getBeacon] Start");

        if (ismbRestService.authenticate(deviceToken)) {

            LOG.info("[getBeacon] End");
            return new ResponseEntity<>(new Gson().toJson(ismbRestService.getBeacon()), HttpStatus.OK);
        }

        LOG.info("[getBeacon] End");
        return new ResponseEntity<>(new Gson().toJson(new ArrayList<>()), HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/getArea", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> getArea(@RequestHeader(value = "Device-Token") String deviceToken) {
        LOG.info("[getArea] Start");

        if (ismbRestService.authenticate(deviceToken)) {

            LOG.info("[getArea] End");
            return new ResponseEntity<>(new Gson().toJson(ismbRestService.getArea()), HttpStatus.OK);
        }

        LOG.info("[getArea] End");
        return new ResponseEntity<>(new Gson().toJson(new ArrayList<>()), HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> uploadImg(@RequestBody ChatMessage chatMessage) {
        LOG.info("[getArea] Start");

        if (chatMessage != null) {
            return new ResponseEntity<>(new Gson().toJson(ismbRestService.saveImg(chatMessage)), HttpStatus.OK);
        }

        LOG.info("[getArea] End");
        return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
    }
}