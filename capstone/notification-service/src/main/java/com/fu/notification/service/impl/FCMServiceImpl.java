package com.fu.notification.service.impl;

import com.fu.common.constant.KeyConstant;
import com.fu.common.util.AESUtil;
import com.fu.common.util.JSONUtil;
import com.fu.notification.model.FCMNotification;
import com.fu.notification.model.MessageBody;
import com.fu.notification.service.FCMService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class FCMServiceImpl implements FCMService {

    private static final Logger LOG = Logger.getLogger(FCMServiceImpl.class);

    private final Properties properties;

    @Autowired
    public FCMServiceImpl(Properties properties) {
        this.properties = properties;
    }

    /**
     * Send notification to devices
     *
     * @param title
     * @param message
     * @param receiver
     */
    @Override
    public void sendNotificationMessage(String title, String message, String receiver) {
        LOG.info("[sendNotificationMessage] Start: title = " + title);

        String key = properties.getProperty("aes.key") + KeyConstant.AES_KEY;

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", AESUtil.decryptByAES(properties.getProperty("fcm.project.key"), key));

        FCMNotification notification = new FCMNotification();
        notification.setTitle(title);
        notification.setText(message);

        MessageBody body = new MessageBody();
        body.setNotification(notification);
        body.setTo(receiver);

        String bodyString = JSONUtil.ojbToJson(body);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpEntity<String> request = new HttpEntity<>(bodyString, headers);
        restTemplate.postForObject(properties.getProperty("fcm.message.send"), request, String.class);

        LOG.info("[sendNotificationMessage] End");
    }

    @Override
    public void sendDataMessage(String data, String receiver) {
        LOG.info("[sendDataMessage] Start");

        String key = properties.getProperty("aes.key") + KeyConstant.AES_KEY;

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", AESUtil.decryptByAES(properties.getProperty("fcm.project.key"), key));

        Map<String,String> dataMap = new HashMap<>();
        dataMap.put("data",data);

        MessageBody body = new MessageBody();
        body.setData(dataMap);
        body.setTo(receiver);

        String bodyString = JSONUtil.ojbToJson(body);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpEntity<String> request = new HttpEntity<>(bodyString, headers);
        restTemplate.postForObject(properties.getProperty("fcm.message.send"), request, String.class);

        LOG.info("[sendDataMessage] End");
    }
}
