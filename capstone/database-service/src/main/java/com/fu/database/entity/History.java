package com.fu.database.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by manlm on 9/20/2016.
 */
@Entity
@Table(name = "history")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "productId")
    @NotNull
    private long productId;

    @Column(name = "productName")
    @NotNull
    private String productName;

    @Column(name = "customerAccountId")
    @NotNull
    private long customerAccountId;

    @Column(name = "botFbId")
    @NotNull
    private String botFbId;

    @Column(name = "appFbId")
    @NotNull
    private String appFbId;

    @Column(name = "date")
    @NotNull
    private long date;

    public History() {
        // Default Constructor
    }

    /**
     * Constructor
     *
     * @param productId
     * @param productName
     * @param customerAccountId
     * @param botFbId
     * @param appFbId
     * @param date
     */
    public History(long productId, String productName, long customerAccountId, String botFbId, String appFbId, long date) {
        this.productId = productId;
        this.productName = productName;
        this.customerAccountId = customerAccountId;
        this.botFbId = botFbId;
        this.appFbId = appFbId;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getCustomerAccountId() {
        return customerAccountId;
    }

    public void setCustomerAccountId(long customerAccontId) {
        this.customerAccountId = customerAccontId;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}