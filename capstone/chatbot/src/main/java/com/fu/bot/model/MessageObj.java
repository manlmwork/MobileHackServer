package com.fu.bot.model;

/**
 * Created by DatHT on 10/29/2016.
 * Email: datht0601@gmail.com
 */

public class MessageObj {
    private String text;
    private byte[] image;
    private String thumb;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
