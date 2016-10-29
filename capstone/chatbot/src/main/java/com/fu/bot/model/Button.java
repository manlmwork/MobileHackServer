package com.fu.bot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Button {

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("payload")
    @Expose
    private String payload;

    @SerializedName("url")
    @Expose
    private String url;

    public Button() {
        // default constructor
    }

    public Button(String type, String title, String payload) {
        this.type = type;
        this.title = title;
        this.payload = payload;
    }

    public Button(String type, String title, String payload, String url) {
        this.type = type;
        this.title = title;
        this.payload = payload;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
