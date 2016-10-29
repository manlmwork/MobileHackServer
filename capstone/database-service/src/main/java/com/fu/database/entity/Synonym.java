package com.fu.database.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by manlm on 9/17/2016.
 */
@Entity
@Table(name = "synonym")
public class Synonym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "synonymId")
    @NotNull
    private int synonymId;

    public Synonym() {
        // Default Constructor
    }

    /**
     * Constructor
     *
     * @param name
     * @param synonymId
     */
    public Synonym(String name, int synonymId) {
        this.name = name;
        this.synonymId = synonymId;
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

    public int getSynonymId() {
        return synonymId;
    }

    public void setSynonymId(int synonymId) {
        this.synonymId = synonymId;
    }
}