package com.fu.bot.service;

public interface FacebookAPIService {

    void doSubscribeRequest();

    void createGetStartButton();

    void createGreetingText();

    void createPersistentMenu();

    void sendTextMessage(String userId, String text);

    void sendTextMessage(String jsonString);
}
