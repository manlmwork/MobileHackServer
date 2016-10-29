package com.fu.common.config;

import com.fu.common.constant.KeyConstant;
import com.fu.common.util.AESUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by manlm on 8/16/2016.
 */
public class EncryptionAwarePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private static final Logger LOG = Logger.getLogger(EncryptionAwarePropertyPlaceholderConfigurer.class);

    @Override
    protected String convertPropertyValue(String originalValue) {
        LOG.info("[convertPropertyValue] Start");
        if (originalValue.startsWith("{AES}")) {
            LOG.info("[convertPropertyValue] End");
            return decrypt(originalValue.substring(5));
        }
        LOG.info("[convertPropertyValue] End");
        return originalValue;
    }

    private String decrypt(String value) {
        LOG.info("[decrypt] Start");
        try {
            Properties prop = new Properties();
            InputStream input = EncryptionAwarePropertyPlaceholderConfigurer.class.getClassLoader().getResourceAsStream("aes.properties");
            prop.load(input);
            LOG.info("[decrypt] End");
            return AESUtil.decryptByAES(value, prop.getProperty("aes.key") + KeyConstant.AES_KEY);
        } catch (FileNotFoundException e) {
            LOG.error("[decrypt] FileNotFoundException: " + e);
            return "";
        } catch (IOException e) {
            LOG.error("[decrypt] IOException: " + e);
            return "";
        }
    }
}
