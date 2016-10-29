package com.fu.database.dao.impl;

import com.fu.database.dao.DeviceTokenDao;
import com.fu.database.entity.DeviceToken;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by manlm on 9/25/2016.
 */
@Repository
public class DeviceTokenDaoImpl extends GenericDaoImpl<DeviceToken, String> implements DeviceTokenDao {

    private static final Logger LOG = Logger.getLogger(DeviceTokenDaoImpl.class);

    @Override
    public List<String> getDeviceTokenByBotFbId(String botFbId) {
        LOG.info("[getDeviceTokenByBotFbId] Start: userId " + botFbId);

        LOG.info("[getDeviceTokenByBotFbId] End");
        return getEntityManager().createQuery("SELECT d.token FROM DeviceToken d WHERE d.botFbId=:botFbId", String.class)
                .setParameter("botFbId", botFbId).getResultList();
    }

    @Override
    public int insertBotFbIdByAppFbId(String botFbId, String appFbId) {
        LOG.info("[insertBotFbIdByAppFbId] Start");
        LOG.info("[insertBotFbIdByAppFbId] End");
        return getEntityManager().createQuery("UPDATE " + DeviceToken.class.getSimpleName()
                + " d SET d.botFbId = :botFbId WHERE d.appFbId = :appFbId")
                .setParameter("botFbId", botFbId)
                .setParameter("appFbId", appFbId)
                .executeUpdate();
    }

}
