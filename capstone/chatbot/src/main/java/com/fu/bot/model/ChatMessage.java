package com.fu.bot.model;

/**
 * Created by DatHT on 10/29/2016.
 * Email: datht0601@gmail.com
 */

public class ChatMessage {
    private String name;
    private MessageObj mess;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MessageObj getMess() {
        return mess;
    }

    public void setMess(MessageObj mess) {
        this.mess = mess;
    }
}
