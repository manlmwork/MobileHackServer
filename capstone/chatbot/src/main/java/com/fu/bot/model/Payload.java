package com.fu.bot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Payload {

    @SerializedName("template_type")
    @Expose
    private String templateType;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("elements")
    @Expose
    private List<Element> elements;

    @SerializedName("buttons")
    @Expose
    private List<Button> buttons;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("typeTrend")
    @Expose
    private String typeTrend;

    @SerializedName("typeMore")
    @Expose
    private String typeMore;

    @SerializedName("typeMenu")
    @Expose
    private String typeMenu;

    @SerializedName("typeButton")
    @Expose
    private String typeButton;

    @SerializedName("positionHistory")
    @Expose
    private String positionHistory;

    @SerializedName("timeHistory")
    @Expose
    private String timeHistory;

    //tracking position
    @SerializedName("positionShow")
    @Expose
    private String positionShow;

    @SerializedName("typeShow")
    @Expose
    private String typeShow;

    @SerializedName("nameProduct")
    @Expose
    private String nameProduct;

    @SerializedName("userId")
    @Expose
    private String userId;
    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTypeTrend() {
        return typeTrend;
    }

    public void setTypeTrend(String typeTrend) {
        this.typeTrend = typeTrend;
    }

    public String getTypeMore() {
        return typeMore;
    }

    public void setTypeMore(String typeMore) {
        this.typeMore = typeMore;
    }

    public String getTypeMenu() {
        return typeMenu;
    }

    public void setTypeMenu(String typeMenu) {
        this.typeMenu = typeMenu;
    }

    public String getTypeButton() {
        return typeButton;
    }

    public void setTypeButton(String typeButton) {
        this.typeButton = typeButton;
    }

    public String getPositionHistory() {
        return positionHistory;
    }

    public void setPositionHistory(String positionHistory) {
        this.positionHistory = positionHistory;
    }

    public String getTimeHistory() {
        return timeHistory;
    }

    public void setTimeHistory(String timeHistory) {
        this.timeHistory = timeHistory;
    }

    public String getPositionShow() {
        return positionShow;
    }

    public void setPositionShow(String positionShow) {
        this.positionShow = positionShow;
    }

    public String getTypeShow() {
        return typeShow;
    }

    public void setTypeShow(String typeShow) {
        this.typeShow = typeShow;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
