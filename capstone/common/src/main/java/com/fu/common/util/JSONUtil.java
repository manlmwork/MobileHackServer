package com.fu.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by manlm on 9/2/2016.
 */
public class JSONUtil {

    private static final Logger LOG = Logger.getLogger(JSONUtil.class);

    private JSONUtil() {
    }

    /**
     * convert object to json
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String ojbToJson(T obj) {
        LOG.info("[ojbToJson] Start");
        ObjectMapper mapper = new ObjectMapper();
        try {
            LOG.info("[ojbToJson] End");
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOG.error("[ojbToJson] JsonProcessingException: " + e);
            return null;
        }
    }

    /**
     * Convert json to object
     *
     * @param jsonString
     * @param baseCls
     * @param paramCls
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToObj(String jsonString, Class<T> baseCls, Class<?>... paramCls) {
        LOG.info("[jsonToObj] Start");
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = TypeFactory.defaultInstance().constructParametrizedType(String.class, baseCls, paramCls);
        try {
            LOG.info("[jsonToObj] End");
            return (T) mapper.readValue(jsonString, type);
        } catch (IOException e) {
            LOG.error("[jsonToObj] IOException: " + e);
            return null;
        }
    }
}
