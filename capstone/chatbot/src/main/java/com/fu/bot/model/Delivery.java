package com.fu.bot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Delivery {

    @SerializedName("mids")
    @Expose
    private List<String> mids = new ArrayList<>();

    @SerializedName("watermark")
    @Expose
    private Long watermark;

    @SerializedName("seq")
    @Expose
    private Long seq;

    public List<String> getMids() {
        return mids;
    }

    public void setMids(List<String> mids) {
        this.mids = mids;
    }

    public Long getWatermark() {
        return watermark;
    }

    public void setWatermark(Long watermark) {
        this.watermark = watermark;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }
}
