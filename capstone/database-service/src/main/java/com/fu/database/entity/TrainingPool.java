package com.fu.database.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by manlm on 9/21/2016.
 */
@Entity
@Table(name = "trainingpool")
public class TrainingPool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "userSay")
    @NotNull
    private String userSay;

    public TrainingPool() {
        // Default Constructor
    }

    /**
     * Constructor
     *
     * @param userSay
     */
    public TrainingPool(String userSay) {
        this.userSay = userSay;
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
}
