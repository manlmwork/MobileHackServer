package com.fu.bot.service.impl;

import com.fu.bot.model.ChatMessage;
import com.fu.bot.model.MessageObj;
import com.fu.bot.model.ProductObj;
import com.fu.bot.model.ResponeObj;
import com.fu.bot.service.AccentizerService;
import com.fu.bot.service.FireBaseService;
import com.fu.bot.utils.Constant;
import com.fu.bot.utils.FirebaseUtils;
import com.fu.database.dao.ProductDao;
import com.fu.database.entity.Product;
import com.fu.nlp.service.NaturalLanguageProcessingService;
import com.fu.vision.service.VisionService;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.aspectj.bridge.Version.text;

@Service
public class FireBaseServiceImpl implements FireBaseService {

    private static final Logger logger = Logger.getLogger(FireBaseServiceImpl.class);
    private static final String serviceAccountCredentials = "serviceAccountCredentials.json";

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(FirebaseUtils.class);
    private final NaturalLanguageProcessingService naturalLanguageProcessingService;
    private final AccentizerService accentizerService;
    private final ProductDao productDao;
    private final VisionService visionService;

    public FireBaseServiceImpl(NaturalLanguageProcessingService naturalLanguageProcessingService, AccentizerService accentizerService, ProductDao productDao, VisionService visionService) {
        this.naturalLanguageProcessingService = naturalLanguageProcessingService;
        this.accentizerService = accentizerService;
        this.productDao = productDao;
        this.visionService = visionService;
    }

    public List<Product> handleImageFromUser(byte[] imageData) {
        logger.info("[handleImageFromUser] - Start");
        List<EntityAnnotation> logos = visionService.detectLogo(imageData, 0);
        String responseText = logos.get(0).getDescription();

        List<Product> listProduct = productDao.getProductBySearchName(responseText, 0, 10);

        logger.info("[handleImageFromUser] - End");
        return listProduct;
    }

    public List<Product> handleTextFromUser(String textMessage) {
        LOG.info("[handleTextFromUser] Start ");
        // normal text
        String responseText = naturalLanguageProcessingService.processSpeech(accentizerService.add(textMessage));
        List<Product> list = productDao.getProductBySearchName(responseText, 0, 10);

        LOG.info("[handleTextFromUser] End ");
        return list;
    }

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
                logger.info("[onChildAdded] - Start: chatname=" + chatMessage.getName());
                if (!chatMessage.getName().equalsIgnoreCase("SERVER")) {
                    logger.info("[onChildAdded] - Check if SERVER - Start");
                    MessageObj messageObj = chatMessage.getMess();
                    if (messageObj != null) {
                        System.out.println("HuyTCM: mess = " + messageObj.getText());
                        ResponeObj responeObj = new ResponeObj();
                        List<Product> productList = null;
                        if (messageObj.getText() != null) {
                            if (!messageObj.getText().isEmpty()) {
                                productList = handleTextFromUser(messageObj.getText());
                            }
                        } else if (messageObj.getImage() != null) {
                            if (messageObj.getImage().length > 0) {
                                productList = handleImageFromUser(messageObj.getImage());
                            }
                        }

                        if (productList == null) {
                            responeObj.setMess("text");
                        } else if(productList.isEmpty()){
                            responeObj.setMess("text");
                        }else {
                            List<ProductObj> listProductObj = new ArrayList<>();
                            for (Product product: productList) {
                                ProductObj productObj = new ProductObj();
                                productObj.setName(product.getName());
                                productObj.setUrl(product.getImgUrl());

                                listProductObj.add(productObj);
                            }
                            responeObj.setProductObjList(listProductObj);
                        }
                        saveDataToFirebaseDatabase(responeObj);
                    }
                    logger.info("[onChildAdded] - Check if SERVER - End");
                }
                logger.info("[onChildAdded] - End");
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

    private void saveDataToFirebaseDatabase(ResponeObj respone) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("/");

        DatabaseReference userRef = ref.child(ref.push().getKey());

        userRef.setValue(respone);
    }


}
