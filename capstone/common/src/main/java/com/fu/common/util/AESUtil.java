package com.fu.common.util;

import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Base64;

/**
 * Created by manlm on 7/25/2016.
 */
public class AESUtil {

    private static final Logger LOG = Logger.getLogger(AESUtil.class);

    private AESUtil() {
    }

    /**
     * encrypt a string
     *
     * @param inputString
     * @param key
     * @return
     */
    public static String encryptByAES(String inputString, String key) {
        LOG.info("[encryptByAES] Start");
        if (inputString != null) {
            String encryptedValue;
            try {
                Key genKey = generateKey(key);
                Cipher c = Cipher.getInstance("AES");
                c.init(Cipher.ENCRYPT_MODE, genKey);
                byte[] encVal = c.doFinal(inputString.getBytes());
                encryptedValue = Base64.getEncoder().encodeToString(encVal);
            } catch (Exception e) {
                LOG.error("[encryptByAES] Exception: " + e);
                return null;
            }
            LOG.info("[encryptByAES] End");
            return encryptedValue;
        } else {
            LOG.info("[encryptByAES] End");
            return null;
        }
    }

    /**
     * decrypt a string
     *
     * @param inputString
     * @param key
     * @return
     */
    public static String decryptByAES(String inputString, String key) {
        LOG.info("[decryptByAES] Start");
        if (inputString != null) {
            String decryptedValue;
            try {
                Key genKey = generateKey(key);
                Cipher c = Cipher.getInstance("AES");
                c.init(Cipher.DECRYPT_MODE, genKey);
                byte[] decodedValue = Base64.getDecoder().decode(inputString);
                byte[] decValue = c.doFinal(decodedValue);
                decryptedValue = new String(decValue, "UTF-8");

            } catch (Exception e) {
                LOG.info("[decryptByAES] Exception: " + e);
                return null;
            }
            LOG.info("[decryptByAES] End");
            return decryptedValue;
        } else {
            LOG.info("[decryptByAES] End");
            return null;
        }
    }

    /**
     * generate key
     *
     * @param key
     * @return
     * @throws UnsupportedEncodingException
     */
    private static Key generateKey(String key) throws UnsupportedEncodingException {
        return new SecretKeySpec(key.getBytes("UTF-8"), "AES");
    }
}
