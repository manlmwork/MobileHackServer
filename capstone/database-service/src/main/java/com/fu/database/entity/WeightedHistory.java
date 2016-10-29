package com.fu.database.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by manlm on 9/23/2016.
 */
@Entity
@Table(name = "weightedhistory")
public class WeightedHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "productId")
    @NotNull
    private long productId;

    @Column(name = "customerAccontId")
    @NotNull
    private String customerAccontId;

    @Column(name = "botFbId")
    @NotNull
    private String botFbId;

    @Column(name = "appFbId")
    @NotNull
    private String appFbId;

    @Column(name = "weight")
    @NotNull
    private long weight;

    public WeightedHistory() {
        // Default Constructor
    }

    /**
     * Constructor
     *
     * @param productId
     * @param customerAccontId
     * @param botFbId
     * @param appFbId
     * @param weight
     */
    public WeightedHistory(long productId, String customerAccontId, String botFbId, String appFbId, long weight) {
        this.productId = productId;
        this.customerAccontId = customerAccontId;
        this.botFbId = botFbId;
        this.appFbId = appFbId;
        this.weight = weight;
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

    public String getCustomerAccontId() {
        return customerAccontId;
    }

    public void setCustomerAccontId(String customerAccontId) {
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

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }
}
