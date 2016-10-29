package com.fu.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by manlm on 9/25/2016.
 */
@Entity
@Table(name = "devicetoken")
public class DeviceToken {

    @Id
    @Column(name = "token")
    @NotNull
    private String token;

    @Column(name = "customerAccontId")
    @NotNull
    private long customerAccontId;

    @Column(name = "botFbId")
    @NotNull
    private String botFbId;

    @Column(name = "appFbId")
    @NotNull
    private String appFbId;

    public DeviceToken() {
        // Default Constructor
    }

    /**
     * Constructor
     *
     * @param token
     * @param customerAccontId
     * @param botFbId
     * @param appFbId
     */
    public DeviceToken(String token, long customerAccontId, String botFbId, String appFbId) {
        this.token = token;
        this.customerAccontId = customerAccontId;
        this.botFbId = botFbId;
        this.appFbId = appFbId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getCustomerAccontId() {
        return customerAccontId;
    }

    public void setCustomerAccontId(long customerAccontId) {
        this.customerAccontId = customerAccontId;
    }

    public String getBotFbId() {
        return botFbId;
    }

    public void setBotFbId(String botFbId) {
        this.botFbId = botFbId;
    }

    public String getAppFbId() {
        return appFbId;
    }

    public void setAppFbId(String appFbId) {
        this.appFbId = appFbId;
    }
}
