package com.fu.bot.utils;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by HuyTCM on 10/29/16.
 */
public class FirebaseUtils {

    public  static FirebaseApp initialFirebaseApp () throws FileNotFoundException {
        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setServiceAccount(new FileInputStream("/resources/serviceAccountCredentials.json"))
                .setDatabaseUrl("https://ismbhack.firebaseio.com/")
                .build();
        return FirebaseApp.initializeApp(firebaseOptions);
    }


}
