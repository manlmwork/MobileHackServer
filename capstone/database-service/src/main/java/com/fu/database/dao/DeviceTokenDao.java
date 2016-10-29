package com.fu.database.dao;

import com.fu.database.entity.DeviceToken;

import java.util.List;

/**
 * Created by manlm on 9/25/2016.
 */
public interface DeviceTokenDao extends GenericDao<DeviceToken, String> {

    public List<String> getDeviceTokenByBotFbId(String botFbId);

    public int insertBotFbIdByAppFbId(String botFbId, String appFbId);
}
