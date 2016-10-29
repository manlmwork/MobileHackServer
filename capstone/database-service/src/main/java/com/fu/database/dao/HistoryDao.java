package com.fu.database.dao;

import com.fu.database.entity.History;
import com.fu.database.entity.Product;

import java.util.List;

/**
 * Created by manlm on 9/21/2016.
 */
public interface HistoryDao extends GenericDao<History, Long> {
    public List<History> getHistoryByBotFbId(String botFbId, int start, int maxResult);

    public List<History> getHistoryByTime(String botFbId, long millisTime,int start, int maxResult);

    public int getQuantityProductByTime(String botFbId, long millisTime);

    public List<String> getDateHistory(String botFbId);

    public List<Long> getProductIdInSpecificDay(String botFbId, long millisTime);
}
