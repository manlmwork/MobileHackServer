package com.fu.bot.service.impl;

import com.fu.bot.model.ChatMessage;
import com.fu.bot.model.MessageObj;
import com.fu.bot.service.FireBaseService;
import com.fu.bot.utils.Constant;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class FireBaseServiceImpl implements FireBaseService {

    private static final Logger logger = Logger.getLogger(FireBaseServiceImpl.class);

    private static final String serviceAccountCredentials = "serviceAccountCredentials.json";

    @Override
    public void startFirebaseApp() {
        logger.info("[startFirebaseApp] - Start");

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setServiceAccount(classloader.getResourceAsStream(serviceAccountCredentials))
                .setDatabaseUrl(Constant.FIREBASE_DATABASE_URL)
                .build();
        FirebaseApp.initializeApp(firebaseOptions);
        logger.info("[startFirebaseApp] - End");
    }

    @Override
    public void registerDatabaseChildEventListener() {
        logger.info("[registerEventListener] - Start");
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
        logger.info("[registerEventListener] - End");
    }

    private void saveDataToFirebaseDatabase() {

    }
}
