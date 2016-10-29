package com.fu.bot.service.impl;

import com.amazonaws.services.dynamodbv2.xspec.NULL;
import com.fu.bot.model.ChatMessage;
import com.fu.bot.model.MessageObj;
import com.fu.bot.service.FireBaseService;
import com.fu.bot.utils.Constant;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

                ChatMessage chatMessage1 = new ChatMessage();
                chatMessage1.setName("SERVER");

                MessageObj obj = new MessageObj();
                messageObj.setText("HELLO MY NAME IS SERVER");

                chatMessage1.setMess(obj);

                saveDataToFirebaseDatabase(chatMessage1);
                if (!messageObj.getText().isEmpty()) {
                    // Do something

                } else if (messageObj.getImage() != null && messageObj.getImage().length > 0) {
                    // Do something

                } else {
                    // Handle exception
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

    private void saveDataToFirebaseDatabase(ChatMessage chatMessage) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("/");

        DatabaseReference userRef = ref.child("name");

        userRef.setValue(chatMessage);
    }


}
