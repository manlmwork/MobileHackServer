package com.fu.database.dao.impl;

import com.fu.common.constant.KeyConstant;
import com.fu.common.util.DateUtil;
import com.fu.database.dao.HistoryDao;
import com.fu.database.entity.History;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manlm on 9/21/2016.
 */
@Repository
public class HistoryDaoImpl extends GenericDaoImpl<History, Long> implements HistoryDao {

    private static final Logger LOG = Logger.getLogger(HistoryDaoImpl.class);

    public List<History> getHistoryByBotFbId(String botFbId, int start, int maxResult) {
        LOG.info(new StringBuilder("[getHistoryByBotFbId] Start: start = ").append(start)
                .append(", maxResult = ").append(maxResult));
        LOG.info("[getHistoryByBotFbId] End");
        return getEntityManager().createQuery("SELECT p FROM History p WHERE p.botFbId=:botFbId ORDER BY p.date desc ", History.class)
                .setParameter("botFbId", botFbId)
                .setFirstResult(start)
                .setMaxResults(maxResult)
                .getResultList();
    }

    public List<History> getHistoryByTime(String botFbId, long millisTime, int start, int maxResult) {
        LOG.info(new StringBuilder("[getHistoryByTime] Start: millisTime = ").append(millisTime)
                .append(", start = ").append(start)
                .append(", maxResult = ").append(maxResult));
        LOG.info("[getHistoryByTime] End");
        return getEntityManager().createQuery("SELECT p FROM History p WHERE p.botFbId=:botFbId and p.date>:millisTime AND p.date< :millisTime+:oneDay ORDER BY p.date desc", History.class)
                .setParameter("botFbId", botFbId)
                .setParameter("millisTime", millisTime)
                .setFirstResult(start)
                .setMaxResults(maxResult)
                .setParameter("oneDay", KeyConstant.MILLISECOND_ONEDAY)
                .getResultList();
    }

    public int getQuantityProductByTime(String botFbId, long millisTime) {
        LOG.info("[getQuantityProductByTime] millisTime = " + millisTime);
        LOG.info("[getQuantityProductByTime] End");
        return getEntityManager().createQuery("SELECT p FROM History p WHERE p.botFbId=:botFbId and p.date>:millisTime AND p.date< :millisTime+:oneDay", History.class)
                .setParameter("botFbId", botFbId)
                .setParameter("millisTime", millisTime)
                .setParameter("oneDay", KeyConstant.MILLISECOND_ONEDAY)
                .getResultList().size();
    }

    public List<String> getDateHistory(String botFbId) {
        LOG.info("[getDateHistory] Start");

        List<Long> list = getEntityManager().createQuery("SELECT  p.date FROM History p WHERE p.botFbId=:botFbId  ORDER BY p.date desc")
                .setParameter("botFbId", botFbId)
                .getResultList();
        List<String> listDate = new ArrayList<>();

        for (Long date : list) {
            String tmp = DateUtil.parseDateFromMillisecond(date, "dd/MM/yyyy");
            if (!listDate.contains(tmp)) {
                listDate.add(tmp);
            }

        }

        LOG.info("[getDateHistory] End");
        if (listDate.size()>10) {
            return listDate.subList(KeyConstant.BEGIN_SHOW_HISTORY, KeyConstant.MAX_SHOW_HISTORY);
        }
        else
            return listDate;
    }

    public List<Long> getProductIdInSpecificDay(String botFbId, long millisTime) {
        LOG.info("[getProductIdInSpecificDay] Start: millisTime = " + millisTime);
        LOG.info("[getProductIdInSpecificDay] End");
        return (List<Long>) getEntityManager().createQuery("SELECT DISTINCT p.productId FROM History p WHERE p.botFbId=:botFbId and p.date>:millisTime AND p.date< :millisTime+:oneDay")
                .setParameter("botFbId", botFbId)
                .setParameter("millisTime", millisTime)
                .setParameter("oneDay", KeyConstant.MILLISECOND_ONEDAY)
                .getResultList();
    }

}
