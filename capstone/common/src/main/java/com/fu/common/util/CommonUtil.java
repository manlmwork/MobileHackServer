package com.fu.common.util;

import org.apache.log4j.Logger;
import org.springframework.util.SerializationUtils;

import java.nio.charset.StandardCharsets;

/**
 * Created by manlm on 8/20/2016.
 */
public class CommonUtil {

    private static final Logger LOG = Logger.getLogger(CommonUtil.class);

    private CommonUtil() {
    }

    /**
     * Convert an object to byte string
     *
     * @param ojb
     * @param <E>
     * @return
     */
    public static <E> String ojbToByteString(E ojb) {
        LOG.info("[ojbToByteString] Start: ojb = " + ojb.getClass().getSimpleName());
        byte[] data = SerializationUtils.serialize(ojb);
        LOG.info("[ojbToByteString] End");
        return new String(data, StandardCharsets.ISO_8859_1);
    }

    /**
     * Convert an byte string to object
     *
     * @param byteString
     * @return
     */
    public static Object byteStringToOjb(String byteString) {
        LOG.info("[byteStringToOjb] Start: byteString = " + byteString);
        byte[] data = byteString.getBytes(StandardCharsets.ISO_8859_1);
        LOG.info("[byteStringToOjb] End");
        return SerializationUtils.deserialize(data);
    }
}
