package com.fu.bot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Messaging {

    @SerializedName("sender")
    @Expose
    private Sender sender;

    @SerializedName("recipient")
    @Expose
    private Recipient recipient;

    @SerializedName("timestamp")
    @Expose
    private long timestamp;

    @SerializedName("message")
    @Expose
    private Message message;

    @SerializedName("postback")
    @Expose
    private Postback postback;

    @SerializedName("delivery")
    @Expose
    private Delivery delivery;

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public Postback getPostback() {
        return postback;
    }

    public void setPostback(Postback postback) {
        this.postback = postback;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
