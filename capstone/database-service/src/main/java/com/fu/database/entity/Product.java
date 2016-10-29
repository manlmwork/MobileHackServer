package com.fu.database.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by manlm on 9/17/2016.
 */
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "code")
    @NotNull
    private String code;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "imgUrl")
    @NotNull
    private String imgUrl;

    @Column(name = "imgPromotionUrl")
    private String imgPromotionUrl;

    @Column(name = "price")
    @NotNull
    private long price;

    @Column(name = "details")
    @NotNull
    private String details;

    @Column(name = "areaId")
    private int areaId;

    @Column(name = "areaName")
    private String areaName;

    @Column(name = "areaLocation")
    private String areaLocation;

    @Column(name = "areaWeight")
    private int areaWeight;

    @Column(name = "beaconMinor1")
    private int beaconMinor1;

    @Column(name = "beaconMinor2")
    private int beaconMinor2;

    @Column(name = "beaconMinor3")
    private int beaconMinor3;

    @Column(name = "beaconMinor4")
    private int beaconMinor4;

    public Product() {
        // Default Constructor
    }

    /**
     * Constructor
     *
     * @param code
     * @param name
     * @param imgUrl
     * @param imgPromotionUrl
     * @param price
     * @param details
     * @param areaId
     * @param areaName
     * @param areaLocation
     * @param areaWeight
     * @param beaconMinor1
     * @param beaconMinor2
     * @param beaconMinor3
     * @param beaconMinor4
     */
    public Product(String code, String name, String imgUrl, String imgPromotionUrl, long price, String details, int areaId, String areaName, String areaLocation, int areaWeight, int beaconMinor1, int beaconMinor2, int beaconMinor3, int beaconMinor4) {
        this.code = code;
        this.name = name;
        this.imgUrl = imgUrl;
        this.imgPromotionUrl = imgPromotionUrl;
        this.price = price;
        this.details = details;
        this.areaId = areaId;
        this.areaName = areaName;
        this.areaLocation = areaLocation;
        this.areaWeight = areaWeight;
        this.beaconMinor1 = beaconMinor1;
        this.beaconMinor2 = beaconMinor2;
        this.beaconMinor3 = beaconMinor3;
        this.beaconMinor4 = beaconMinor4;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgPromotionUrl() {
        return imgPromotionUrl;
    }

    public void setImgPromotionUrl(String imgPromotionUrl) {
        this.imgPromotionUrl = imgPromotionUrl;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaLocation() {
        return areaLocation;
    }

    public void setAreaLocation(String areaLocation) {
        this.areaLocation = areaLocation;
    }

    public int getAreaWeight() {
        return areaWeight;
    }

    public void setAreaWeight(int areaWeight) {
        this.areaWeight = areaWeight;
    }

    public int getBeaconMinor1() {
        return beaconMinor1;
    }

    public void setBeaconMinor1(int beaconMinor1) {
        this.beaconMinor1 = beaconMinor1;
    }

    public int getBeaconMinor2() {
        return beaconMinor2;
    }

    public void setBeaconMinor2(int beaconMinor2) {
        this.beaconMinor2 = beaconMinor2;
    }

    public int getBeaconMinor3() {
        return beaconMinor3;
    }

    public void setBeaconMinor3(int beaconMinor3) {
        this.beaconMinor3 = beaconMinor3;
    }

    public int getBeaconMinor4() {
        return beaconMinor4;
    }

    public void setBeaconMinor4(int beaconMinor4) {
        this.beaconMinor4 = beaconMinor4;
    }
}