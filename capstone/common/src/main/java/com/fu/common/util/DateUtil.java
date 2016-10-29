package com.fu.common.util;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by manlm on 9/24/2016.
 */
public class DateUtil {

    private static final Logger LOG = Logger.getLogger(DateUtil.class);

    public static String parseDateFromMillisecond(long millisecond, String pattern) {
        LOG.info(new StringBuilder("[parseDateFromMillisecond] Start: millisecond = ").append(millisecond)
                .append(", pattern = ").append(pattern));
        SimpleDateFormat sdfDate = new SimpleDateFormat(pattern);
        Date date = new Date(millisecond);

        LOG.info("[parseDateFromMillisecond] End");
        return sdfDate.format(date);
    }

    public static long getCurUTCInMilliseconds() {
        return System.currentTimeMillis();
    }

    public static long parseMillisecondFromString(String inputDate, String pattern) {
        LOG.info(new StringBuilder("[parseMillisecondFromString] Start: inputDate = ").append(inputDate)
                .append(", pattern = ").append(pattern));
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date = sdf.parse(inputDate);
            LOG.info("[parseMillisecondFromString] End");
            return date.getTime();
        } catch (ParseException e) {
            LOG.error("[parseMillisecondFromString] ParseException: " + e);
            return 0;
        }


    }
}
