package com.fu.common.util;

import org.apache.log4j.Logger;

/**
 * Created by manlm on 9/2/2016.
 */
public class IdUtil {

    private static final Logger LOG = Logger.getLogger(IdUtil.class);

    private static final int ID_LENGTH = 16;

    private IdUtil() {
    }

    public static String generateId() {

        LOG.info("[generateId] Start");
        IdGenerator generator = new IdGenerator();

        LOG.info("[generateId] End");
        return generator.generateId(ID_LENGTH);
    }
}
