package com.fu.database.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by manlm on 9/20/2016.
 */
@Entity
@Table(name = "analizedchatlog")
public class AnalizedChatLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "userSay")
    @NotNull
    private String userSay;

    @Column(name = "counter")
    @NotNull
    private long counter;

    @Column(name = "status")
    @NotNull
    private String status;

    public AnalizedChatLog() {
        // Default Constructor
    }

    /**
     * Constructor
     *
     * @param userSay
     * @param counter
     * @param status
     */
    public AnalizedChatLog(String userSay, long counter, String status) {
        this.userSay = userSay;
        this.counter = counter;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserSay() {
        return userSay;
    }

    public void setUserSay(String userSay) {
        this.userSay = userSay;
    }

    public long getCounter() {
        return counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
