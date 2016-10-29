package com.fu.bot.service.impl;

import com.fu.bot.model.Button;
import com.fu.bot.model.Message;
import com.fu.bot.model.Messaging;
import com.fu.bot.model.Recipient;
import com.fu.bot.service.FacebookAPIService;
import com.fu.bot.utils.Constant;
import com.fu.bot.utils.FBChatHelper;
import com.fu.common.constant.KeyConstant;
import com.fu.common.util.AESUtil;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class FacebookAPIServiceImpl implements FacebookAPIService {

    private static final Logger LOG = Logger.getLogger(FacebookAPIServiceImpl.class);

    private final Properties properties;

    private final FBChatHelper helper;

    private String aesKey;

    @Autowired
    public FacebookAPIServiceImpl(Properties properties, FBChatHelper helper) {
        this.properties = properties;
        this.helper = helper;
        aesKey = properties.getProperty("aes.key") + KeyConstant.AES_KEY;
    }

    /**
     * Subscribe page
     */
    @Override
    public void doSubscribeRequest() {
        LOG.info("[doSubscribeRequest] Start");

        RestTemplate restTemplate = new RestTemplate();

        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory())
                .setConnectTimeout(Integer.parseInt(properties.getProperty(Constant.CONNECTION_TIMEOUT)));

        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        restTemplate.postForObject(properties.getProperty(Constant.SUBSCRIBED_APPS_URL)
                + AESUtil.decryptByAES(properties.getProperty(Constant.PAGE_ACCESS_TOKEN), aesKey), null, String.class);

        LOG.info("[doSubscribeRequest] End");
    }

    /**
     * create get start button
     */
    @Override
    public void createGetStartButton() {
        LOG.info("[createGetStartButton] Start");

        RestTemplate restTemplate = new RestTemplate();

        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory())
                .setConnectTimeout(Integer.parseInt(properties.getProperty(Constant.CONNECTION_TIMEOUT)));

        String callToActions = "[{payload :\"get_start\"}]";

        StringBuilder url = new StringBuilder(properties.getProperty(Constant.THREAD_SETTINGS_URL))
                .append(AESUtil.decryptByAES(properties.getProperty(Constant.PAGE_ACCESS_TOKEN), aesKey))
                .append("&")
                .append(Constant.SETTING_TYPE).append("=").append("call_to_actions")
                .append("&")
                .append("thread_state=").append("new_thread")
                .append("&")
                .append("call_to_actions=").append("{callToActions}");

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        LOG.info(restTemplate.postForObject(String.valueOf(url), null, String.class, callToActions));

        LOG.info("[createGetStartButton] End");
    }

    /**
     * Create greeting text
     */
    @Override
    public void createGreetingText() {
        LOG.info("[createGreetingText] Start");

        RestTemplate restTemplate = new RestTemplate();

        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory())
                .setConnectTimeout(Integer.parseInt(properties.getProperty(Constant.CONNECTION_TIMEOUT)));

        String greeting = "{text: \"Welcome to iSuperMarket Bot!\"}";

        StringBuilder url = new StringBuilder(properties.getProperty(Constant.THREAD_SETTINGS_URL))
                .append(AESUtil.decryptByAES(properties.getProperty(Constant.PAGE_ACCESS_TOKEN), aesKey))
                .append("&")
                .append(Constant.SETTING_TYPE).append("=").append("greeting")
                .append("&")
                .append("greeting=").append("{greeting}");

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        LOG.info(restTemplate.postForObject(String.valueOf(url), null, String.class, greeting));

        LOG.info("[createGreetingText] End");
    }

    /**
     * Create persistent menu
     */
    @Override
    public void createPersistentMenu() {
        LOG.info("[createPersistentMenu] Start");

        RestTemplate restTemplate = new RestTemplate();

        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory())
                .setConnectTimeout(Integer.parseInt(properties.getProperty(Constant.CONNECTION_TIMEOUT)));

        StringBuilder callToActions = createButtonForPersistentMenu();

        StringBuilder url = new StringBuilder(properties.getProperty(Constant.THREAD_SETTINGS_URL))
                .append(AESUtil.decryptByAES(properties.getProperty(Constant.PAGE_ACCESS_TOKEN), aesKey))
                .append("&")
                .append(Constant.SETTING_TYPE).append("=").append("call_to_actions")
                .append("&")
                .append("thread_state=").append("existing_thread")
                .append("&")
                .append("call_to_actions=").append("{callToActions}");

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        LOG.info(restTemplate.postForObject(String.valueOf(url), null, String.class, callToActions));

        LOG.info("[createPersistentMenu] End");
    }

    /**
     * Create buttons for persistence menu
     *
     * @return
     */
    private StringBuilder createButtonForPersistentMenu() {
        LOG.info("[createButtonForPersistentMenu] Start");

        List<Button> buttonList = new ArrayList<>();

        buttonList.add(helper.createShowCartButton());
        buttonList.add(helper.createResetCartButton());
        buttonList.add(helper.createPromotionButton());
        buttonList.add(helper.createHistoryButton());
        buttonList.add(helper.createFeedBackButton());
        StringBuilder callToActions = new StringBuilder("[");

        for (int i = 0; i < buttonList.size(); i++) {
            callToActions.append(new Gson().toJson(buttonList.get(i))).append(",");
        }

        callToActions.append("]");

        LOG.info("[createButtonForPersistentMenu] End");
        return callToActions;
    }

    /**
     * Send text message
     *
     * @param userId
     * @param text
     */
    @Override
    public void sendTextMessage(String userId, String text) {
        LOG.info("[sendTextMessage] Start: userId = " + userId);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");

        Recipient recipient = new Recipient();
        recipient.setId(userId);

        Message message = new Message();
        message.setText(text);

        Messaging messaging = new Messaging();
        messaging.setRecipient(recipient);
        messaging.setMessage(message);

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> request = new HttpEntity<>(new Gson().toJson(messaging), headers);

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        restTemplate.postForObject(properties.getProperty("text_message_url")
                        + AESUtil.decryptByAES(properties.getProperty(Constant.PAGE_ACCESS_TOKEN), aesKey)
                , request, String.class);

        LOG.info("[sendTextMessage] End");
    }

    /**
     * Send text message
     *
     * @param jsonString
     */
    @Override
    public void sendTextMessage(String jsonString) {
        LOG.info("[sendTextMessage] Start");

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        restTemplate.postForObject(properties.getProperty("text_message_url")
                        + AESUtil.decryptByAES(properties.getProperty(Constant.PAGE_ACCESS_TOKEN), aesKey)
                , request, String.class);

        LOG.info("[sendTextMessage] End");
    }
}
