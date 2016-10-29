package com.fu.common.util;

import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by manlm on 7/23/2016.
 */
public class MD5Util {

    private static final Logger LOG = Logger.getLogger(MD5Util.class);

    private MD5Util() {
    }

    /**
     * Convert string to MD5
     *
     * @param s
     * @return
     */
    public static String stringToMD5(String s) {
        LOG.info("[stringToMD5] Start");
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");

            md.update(s.getBytes());

            byte[] byteData = md.digest();

            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                String hex = Integer.toHexString(0xff & byteData[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            LOG.info("[stringToMD5] End");
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            LOG.error("[stringToMD5] NoSuchAlgorithmException: " + e);
            return null;
        }
    }
}
