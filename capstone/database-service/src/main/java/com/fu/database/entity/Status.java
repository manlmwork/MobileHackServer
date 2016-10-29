package com.fu.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by manlm on 9/17/2016.
 */
@Entity
@Table(name = "status")
public class Status {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotNull
    private String name;

    public Status() {
        // Default Constructor
    }

    /**
     * Constructor
     *
     * @param id
     * @param name
     */
    public Status(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
