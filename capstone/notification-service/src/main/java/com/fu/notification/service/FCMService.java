package com.fu.notification.service;

public interface FCMService {

    void sendNotificationMessage(String title, String body, String receiver);

    void sendDataMessage(String data, String receiver);
}
