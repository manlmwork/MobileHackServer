package com.fu.bot.utils;

import com.fu.bot.model.ChatMessage;
import com.fu.bot.model.Message;
import com.fu.bot.model.MessageObj;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.google.firebase.database.Logger;
import org.apache.log4j.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HuyTCM on 10/29/16.
 */
public class FirebaseUtils {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(FirebaseUtils.class);

    public static FirebaseApp initialFirebaseApp() throws FileNotFoundException {

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setServiceAccount(classloader.getResourceAsStream("serviceAccountCredentials.json"))
                .setDatabaseUrl("https://ismbhack.firebaseio.com/")
                .build();
        LOG.info("[initialFirebaseApp] - Start");
        FirebaseApp.initializeApp(firebaseOptions);
        // As an admin, the app has access to read and write all data, regardless of Security Rules
//        DatabaseReference ref = FirebaseDatabase
//                .getInstance()
//                .getReference("/");
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Object document = dataSnapshot.getValue();
//                System.out.println("HuyTCM: " + document);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//        });
        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference("/");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                System.out.println("HuyTCM: text=" + chatMessage.getName());
                MessageObj messageObj = chatMessage.getMess();
                if (messageObj != null) {
                    System.out.println("HuyTCM: mess = " + messageObj.getText());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
        });
        LOG.info("[initialFirebaseApp] - End");
        return null;

    }


}
