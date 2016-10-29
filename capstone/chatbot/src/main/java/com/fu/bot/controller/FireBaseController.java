package com.fu.bot.controller;

import com.fu.bot.service.FireBaseService;
import com.fu.bot.utils.FirebaseUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
public class FireBaseController {

    private static final Logger LOG = Logger.getLogger(WebhookController.class);

    private final FireBaseService fireBaseService;

    private static boolean isStart = false;

    @Autowired
    public FireBaseController(FireBaseService fireBaseService) {
        this.fireBaseService = fireBaseService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void firebaseInit() {
        LOG.info("[firebaseInit] Start");
        if (!isStart) {
            fireBaseService.startFirebaseApp();
            fireBaseService.registerDatabaseChildEventListener();
            isStart = true;
        }
        LOG.info("[firebaseInit] End");

    }
}
