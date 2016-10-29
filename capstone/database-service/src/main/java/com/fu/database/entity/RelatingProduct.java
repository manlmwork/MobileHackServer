package com.fu.database.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by manlm on 9/20/2016.
 */
@Entity
@Table(name = "relatingproduct")
public class RelatingProduct {

    @EmbeddedId
    private RelatingProductKey relatingProductKey;

    @Column(name = "weight")
    @NotNull
    private long weight;

    public RelatingProduct() {
        // Default Constructor
    }

    /**
     * Constructor
     *
     * @param relatingProductKey
     * @param weight
     */
    public RelatingProduct(RelatingProductKey relatingProductKey, long weight) {
        this.relatingProductKey = relatingProductKey;
        this.weight = weight;
    }

    public RelatingProductKey getRelatingProductKey() {
        return relatingProductKey;
    }

    public void setRelatingProductKey(RelatingProductKey relatingProductKey) {
        this.relatingProductKey = relatingProductKey;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }
}
