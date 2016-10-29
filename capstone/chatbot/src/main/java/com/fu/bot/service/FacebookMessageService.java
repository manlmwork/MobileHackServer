package com.fu.bot.service;

import com.fu.bot.model.Messaging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface FacebookMessageService {

    void handleFacebookMessageFromUser(HttpServletRequest request, HttpServletResponse response);

    void handleFacebookMessageText(Messaging event, String userId);

    void handleFacebookMessageImage(String url, String userId);

    void handleFacebookPostback(Messaging event, String userId);
}
