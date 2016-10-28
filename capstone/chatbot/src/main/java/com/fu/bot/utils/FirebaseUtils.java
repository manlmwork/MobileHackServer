package com.fu.bot.utils;

import com.fu.bot.model.ChatMessage;
import com.fu.bot.model.MessageObj;
import com.fu.nlp.service.NaturalLanguageProcessingService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import java.io.FileNotFoundException;

/**
 * Created by HuyTCM on 10/29/16.
 */

public class FirebaseUtils {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(FirebaseUtils.class);

    public static FirebaseApp initialFirebaseApp() throws FileNotFoundException {

        LOG.info("[initialFirebaseApp] - End");
        return null;
    }

}
