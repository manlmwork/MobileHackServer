package com.fu.database.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by manlm on 9/20/2016.
 */
@Entity
@Table(name = "chatlog")
public class ChatLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "userSay")
    @NotNull
    private String userSay;

    @Column(name = "status")
    @NotNull
    private String status;

    public ChatLog() {
        // Default Constructor
    }

    /**
     * Constructor
     *
     * @param userSay
     * @param status
     */
    public ChatLog(String userSay, String status) {
        this.id = id;
        this.userSay = userSay;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
