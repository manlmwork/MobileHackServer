package com.fu.nlp.service.impl;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import com.fu.common.constant.KeyConstant;
import com.fu.common.util.AESUtil;
import com.fu.nlp.service.NaturalLanguageProcessingService;
import com.fu.nlp.utils.Constant;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * Created by manlm on 9/5/2016.
 */
@Service
public class NaturalLanguageProcessingServiceImpl implements NaturalLanguageProcessingService {

    private static final Logger LOG = Logger.getLogger(NaturalLanguageProcessingServiceImpl.class);

    private String aesKey;

    private AIDataService dataService;

    @Autowired
    private NaturalLanguageProcessingServiceImpl(Properties properties) {
        aesKey = properties.getProperty("aes.key") + KeyConstant.AES_KEY;
        AIConfiguration configuration
                = new AIConfiguration(AESUtil.decryptByAES(properties.getProperty("api_ai_access_token"), aesKey));
        dataService = new AIDataService(configuration);
    }

    @Override
    public String processSpeech(String speech) {
        LOG.info("[processSpeech] Start: speech = " + speech);

        try {
            AIRequest request = new AIRequest(speech);
            AIResponse response = dataService.request(request);

            if (response.getStatus().getCode() == 200) {
                LOG.info("[processSpeech] End");
                return response.getResult().getFulfillment().getSpeech();
            } else {
                LOG.info("[processSpeech] End: " + response.getStatus().getErrorDetails());
                return Constant.DO_NOT_UNDERSTAND_MESSAGE;
            }

        } catch (AIServiceException e) {
            LOG.error("[processSpeech] AIServiceException: " + e);
            return Constant.DO_NOT_UNDERSTAND_MESSAGE;
        }
    }
}
