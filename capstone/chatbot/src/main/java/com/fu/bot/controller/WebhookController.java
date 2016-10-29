package com.fu.bot.controller;

import com.fu.bot.service.FacebookAPIService;
import com.fu.bot.service.FacebookMessageService;
import com.fu.bot.service.FireBaseService;
import com.fu.bot.utils.FirebaseUtils;
import com.fu.cache.client.JedisClient;
import com.fu.common.constant.KeyConstant;
import com.fu.common.util.AESUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

@RestController
public class WebhookController {

    private static final Logger LOG = Logger.getLogger(WebhookController.class);

    private final Properties properties;

    private final FacebookAPIService facebookAPIService;

    private final FacebookMessageService facebookMessageService;



    @Autowired
    public WebhookController(Properties properties, FacebookAPIService facebookAPIService, FacebookMessageService facebookMessageService, JedisClient jedisClient) {
        this.properties = properties;
        this.facebookAPIService = facebookAPIService;
        this.facebookMessageService = facebookMessageService;

    }



    @RequestMapping(value = "/webhook", method = RequestMethod.GET)
    public void webhookGet(@RequestParam(name = "hub.verify_token") String verifyToken,
                           @RequestParam(name = "hub.challenge") String challenge,
                           HttpServletResponse response) throws InterruptedException {
        LOG.info("[webhookGet] Start");

        try {
            String aesKey = properties.getProperty("aes.key") + KeyConstant.AES_KEY;

            if (verifyToken.equals(AESUtil.decryptByAES(properties.getProperty("verify_token"), aesKey))) {

                response.getWriter().write(challenge);
                response.getWriter().flush();
                response.getWriter().close();
                response.setStatus(HttpServletResponse.SC_OK);

                Thread.sleep(3000);

                facebookAPIService.doSubscribeRequest();
                facebookAPIService.createGetStartButton();
                facebookAPIService.createGreetingText();
                facebookAPIService.createPersistentMenu();

            } else {
                response.getWriter().write("");
                response.getWriter().flush();
                response.getWriter().close();
                response.setStatus(HttpServletResponse.SC_OK);
            }

            LOG.info("[webhookGet] End");
        } catch (IOException e) {
            LOG.error("[webhookGet] IOException : " + e);
        }
    }

    @RequestMapping(value = "/webhook", method = RequestMethod.POST)
    public void webhookPost(HttpServletRequest request,
                            HttpServletResponse response) throws InterruptedException {
        LOG.info("[webhookPost] Start");
        facebookMessageService.handleFacebookMessageFromUser(request, response);



        LOG.info("[webhookPost] End");
    }
}
