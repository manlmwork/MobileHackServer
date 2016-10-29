package com.fu.database.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by manlm on 9/15/2016.
 */
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "botFbId")
    @NotNull
    private String botFbId;

    @Column(name = "appFbId")
    @NotNull
    private String appFbId;

    @Column(name = "phone")
    private String phone;

    @Column(name = "firstName")
    @NotNull
    private String firstName;

    @Column(name = "lastName")
    @NotNull
    private String lastName;

    public Customer() {
        // Default Constructor
    }

    /**
     * Constructor
     *
     * @param botFbId
     * @param appFbId
     * @param phone
     * @param firstName
     * @param lastName
     */
    public Customer(String botFbId, String appFbId, String phone, String firstName, String lastName) {
        this.botFbId = botFbId;
        this.appFbId = appFbId;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
