package com.fu.database.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by manlm on 9/20/2016.
 */
@Entity
@Table(name = "promotion")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "discountRate")
    private int discountRate;

    @Column(name = "details")
    @NotNull
    private String details;

    @Column(name = "startDate")
    @NotNull
    private long startDate;

    @Column(name = "endDate")
    @NotNull
    private long endDate;

    public Promotion() {
        // Default Constructor
    }

    /**
     * Constructor
     *
     * @param name
     * @param discountRate
     * @param details
     * @param startDate
     * @param endDate
     */
    public Promotion(String name, int discountRate, String details, long startDate, long endDate) {
        this.name = name;
        this.discountRate = discountRate;
        this.details = details;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(int discountRate) {
        this.discountRate = discountRate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }
}