package com.fu.bot.service.impl;

import com.fu.bot.service.AccentizerService;
import com.fu.bot.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Service
public class AccentizerServiceImpl  implements AccentizerService{
    private static final Logger LOG = Logger.getLogger(AccentizerServiceImpl.class);

    private final Properties properties;

    @Autowired
    public AccentizerServiceImpl(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String add(String input) {
        LOG.info("[add] Start: inpuit = " + input);
        RestTemplate restTemplate = new RestTemplate();

        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory())
                .setConnectTimeout(Integer.parseInt(properties.getProperty(Constant.CONNECTION_TIMEOUT)));

        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        LOG.info("[add] End");
        return restTemplate.getForObject(properties.get("vietnamese_accentizer") + "/?text=" + input, String.class);
    }

}
