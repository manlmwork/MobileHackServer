package com.fu.bot.service.impl;

import com.fu.bot.model.*;
import com.fu.bot.service.AccentizerService;
import com.fu.bot.service.FacebookAPIService;
import com.fu.bot.service.FacebookMessageService;
import com.fu.bot.utils.Constant;
import com.fu.bot.utils.FBChatHelper;
import com.fu.cache.client.JedisClient;
import com.fu.common.constant.KeyConstant;
import com.fu.common.util.AESUtil;
import com.fu.common.util.DateUtil;
import com.fu.database.dao.*;
import com.fu.database.entity.*;
import com.fu.nlp.service.NaturalLanguageProcessingService;
import com.fu.notification.service.FCMService;
import com.fu.vision.service.VisionService;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;

@Service
public class FacebookMessageServiceImpl implements FacebookMessageService {

    private static final Logger LOG = Logger.getLogger(FacebookMessageServiceImpl.class);

    private final Properties properties;

    private final FacebookAPIService facebookAPIService;

    private final FBChatHelper helper;

    private final CustomerDao customerDao;

    private String aesKey;

    private final JedisClient jedisClient;

    private final ProductDao productDao;

    private final PromotionDao promotionDao;

    private final ChatLogDao chatlogDao;

    private final NaturalLanguageProcessingService naturalLanguageProcessingService;

    private final HistoryDao historyDao;

    private final FCMService fcmService;

    private final DeviceTokenDao deviceTokenDao;

    private final AccentizerService accentizerService;

    private final VisionService visionService;

    @Autowired
    public FacebookMessageServiceImpl(Properties properties, FacebookAPIService facebookAPIService, FBChatHelper helper,
                                      CustomerDao customerDao, JedisClient jedisClient,
                                      ProductDao productDao, PromotionDao promotionDao, ChatLogDao chatlogDao,
                                      NaturalLanguageProcessingService naturalLanguageProcessingService,
                                      HistoryDao historyDao, FCMService fcmService, DeviceTokenDao deviceTokenDao, AccentizerService accentizerService, VisionService visionService) {
        this.properties = properties;
        this.facebookAPIService = facebookAPIService;
        this.aesKey = properties.getProperty("aes.key") + KeyConstant.AES_KEY;
        this.helper = helper;
        this.customerDao = customerDao;
        this.jedisClient = jedisClient;
        this.productDao = productDao;
        this.promotionDao = promotionDao;
        this.chatlogDao = chatlogDao;
        this.naturalLanguageProcessingService = naturalLanguageProcessingService;
        this.historyDao = historyDao;
        this.fcmService = fcmService;
        this.deviceTokenDao = deviceTokenDao;
        this.accentizerService = accentizerService;
        this.visionService = visionService;
    }

    /**
     * Handle message
     *
     * @param request
     * @param response
     */
    @Override
    public void handleFacebookMessageFromUser(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("[handleFacebookMessageFromUser] Start");

        try {
            /**
             * store the request body in stringbuilder
             */
            StringBuilder body = new StringBuilder();
            String line;

            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }

            /**
             * convert the string request body in java object
             */
            FbMsgRequest fbMsgRequest = new Gson().fromJson(body.toString(), FbMsgRequest.class);

            if (fbMsgRequest == null) {
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }

            List<Messaging> messagings = fbMsgRequest.getEntry().get(0).getMessaging();

            for (Messaging event : messagings) {
                String sender = event.getSender().getId();
                Message message = event.getMessage();

                if (message != null) {
                    if (message.getText() != null) {
                        handleFacebookMessageText(event, sender);
                    } else if (message.getAttachments() != null) {
                        Attachment attachment = message.getAttachments().get(0);
                        if ("image".equals(attachment.getType())) {
                            handleFacebookMessageImage(attachment.getPayload().getUrl(), sender);
                        }
                    }

                } else if (event.getPostback() != null) {
                    handleFacebookPostback(event, sender);
                }
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            LOG.error("[handleFacebookMessageFromUser] IOException : " + e);
        }
        LOG.info("[handleFacebookMessageFromUser] End");
    }

    /**
     * Handle user send text message
     *
     * @param event
     * @param userId
     */
    @Override
    public void handleFacebookMessageText(Messaging event, String userId) {
        LOG.info("[handleFacebookMessageText] Start: userId = " + userId);

        String text = event.getMessage().getText();

        jedisClient.set(userId + "_Say", text, 3600);
        if (text.startsWith(Constant.SHORT_KEY_HISTORY_INPUT)) {
            String[] parts = text.split(":");
            String time = parts[Constant.TEXT_INPUT_HISTORY];
            showHistory(userId, time, String.valueOf(Constant.BEGIN_HISTORY));
        } else if (text.startsWith(Constant.SHORT_KEY.TYPE.getValue())) {

            // short key
            handleShortKey(text, userId);
        } else if (text.startsWith(Constant.PHONE_PREFIX)) {

            // phone
            savePhoneNumber(userId, text.substring(1));
        } else {

            // normal text
            String responseText = naturalLanguageProcessingService.processSpeech(accentizerService.add(text));
            if (Constant.GREETING.equals(responseText)) {
                sendWelcomeMessage(userId);
            } else {
                getProductsMessage(userId, responseText, String.valueOf(Constant.BEGIN_SHOW));
            }
        }

        LOG.info("[handleFacebookMessageText] End");
    }

    @Override
    public void handleFacebookMessageImage(String url, String userId) {
        LOG.info("[handleFacebookMessageImage] Start: url = " + url);
        InputStream is = null;
        try {
            URL u = new URL(url);
            is = u.openStream();
            byte[] imageBytes = IOUtils.toByteArray(is);
            List<EntityAnnotation> logos = visionService.detectLogo(imageBytes, 0);
//            List<EntityAnnotation> texts = visionService.detectText(imageBytes, 0);

            StringBuilder respsone = new StringBuilder("Logo:\n");

            for (EntityAnnotation logo : logos) {
                respsone.append(logo.getDescription()).append("\n");
            }

//            respsone.append("Text:\n");
//
//            for (EntityAnnotation text : texts) {
//                respsone.append(text.getDescription()).append("\n");
//            }

            facebookAPIService.sendTextMessage(userId, String.valueOf(respsone));
            LOG.info("[handleFacebookMessageImage] End");
        } catch (MalformedURLException e) {
            LOG.info("[handleFacebookMessageImage] MalformedURLException: " + e);
        } catch (IOException e) {
            LOG.info("[handleFacebookMessageImage] IOException: " + e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.info("[handleFacebookMessageImage] IOException: " + e);
                }
            }
        }
    }

    /**
     * Handle short key
     *
     * @param text
     * @param userId
     */
    private void handleShortKey(String text, String userId) {
        LOG.info("[handleShortKey] Start: text = " + text);

        if (Constant.SHORT_KEY.SHOW_CART.getValue().equals(text)) {
            handleFacebookMenuPostbackShowCart(userId);
        } else if (Constant.SHORT_KEY.RESET.getValue().equals(text)) {
            handleFacebookMenuPostbackResetCart(userId);
        } else if (Constant.SHORT_KEY.HISTORY.getValue().equals(text)) {
            getProductsHistory(userId);
        } else if (Constant.SHORT_KEY.PROMOTION.getValue().equals(text)) {
            handleFacebookMenuPostbackPromotion(userId);
        }

        LOG.info("[handleShortKey] End");
    }

    /**
     * Save user phone number
     *
     * @param userId
     * @param phone
     */
    private void savePhoneNumber(String userId, String phone) {
        LOG.info("[savePhoneNumber] Start");
        Customer customer = customerDao.getByPhone(phone);

        if (customer != null) {
            if ("".equals(customer.getBotFbId()) && !userId.equals(customer.getBotFbId())) {
                customer.setBotFbId(userId);
                customerDao.update(customer);
                deviceTokenDao.insertBotFbIdByAppFbId(userId, customer.getAppFbId());
                facebookAPIService.sendTextMessage(userId, Constant.SAVED_PHONE);

            } else {
                facebookAPIService.sendTextMessage(userId, Constant.PHONE_ALREADY_REGISTERED);
            }
        } else {
            customer = customerDao.getByBotFBId(userId);
            if (customer == null) {
                FbProfile profile = getProfile(userId);

                customer = new Customer();
                customer.setBotFbId(userId);
                customer.setPhone(phone);
                customer.setFirstName(profile.getFirstName());
                customer.setLastName(profile.getLastName());
                customer.setAppFbId("");
                customerDao.insert(customer);
            } else {
                customerDao.insertPhoneByBotFbId(userId, phone);
            }

            facebookAPIService.sendTextMessage(userId, Constant.SAVED_PHONE);
        }

        LOG.info("[savePhoneNumber] End");
    }

    /**
     * Send welcome message to user
     *
     * @param userId
     */
    private void sendWelcomeMessage(String userId) {
        LOG.info("[sendWelcomeMessage] Start");

        FbProfile profile = getProfile(userId);

        Customer customer = customerDao.getByBotFBId(userId);

        // insert new account
        if (customer == null) {
            customer = customerDao.insert(
                    new Customer(userId, "", "", profile.getFirstName(), profile.getLastName()));
        }

        StringBuilder msg = new StringBuilder(Constant.HELLO_MESSAGE).append(" ")
                .append(profile.getFirstName()).append(" ").append(profile.getLastName()).append(", ")
                .append(Constant.WELCOME_MESSAGE).append("\n");

        if ("".equals(customer.getPhone())) {
            msg.append(Constant.ASK_PHONE_MESSAGE);
        }

        facebookAPIService.sendTextMessage(userId, String.valueOf(msg));
        LOG.info("[sendWelcomeMessage] End");
    }

    /**
     * Get user's profile
     *
     * @param userId
     * @return
     */
    private FbProfile getProfile(String userId) {
        LOG.info("[getProfile] Start");
        String link = StringUtils.replace(properties.getProperty("profile_url")
                        + AESUtil.decryptByAES(properties.getProperty(Constant.PAGE_ACCESS_TOKEN), aesKey)
                , "SENDER_ID", userId);
        LOG.info("[getProfile] End");
        return FBChatHelper.getObjectFromUrl(link, FbProfile.class);
    }

    /**
     * Handle user tap button
     *
     * @param event
     * @param userId
     */
    @Override
    public void handleFacebookPostback(Messaging event, String userId) {
        LOG.info("[handleFacebookPostback] Start: userId = " + userId);

        if (Constant.GET_START.equals(event.getPostback().getPayload())) {
            sendWelcomeMessage(userId);
        } else {
            Map<String, String> map = new HashMap<>();
            map = new Gson().fromJson(event.getPostback().getPayload(), map.getClass());

            String type = map.get("type");

            if (Constant.POST_BACK_TYPE.TYPE_BUTTON.getValue().equals(type)) {
                handleMessageButton(map.get("typeButton"), userId, map);
            } else if (Constant.POST_BACK_TYPE.TYPE_MENU.getValue().equals(type)) {
                handleMenuButton(map.get("typeMenu"), userId);
            }
        }

        LOG.info("[handleFacebookPostback] End");
    }

    /**
     * Handle button on message
     *
     * @param typeButton
     * @param userId
     * @param map
     */
    private void handleMessageButton(String typeButton, String userId, Map<String, String> map) {
        LOG.info("[handleMessageButton] Start: typeButton = " + typeButton);
        List<SaveData> list = (List<SaveData>) jedisClient.get(userId + Constant.CART_POST_FIX);
        List<Long> listId = null;
        if (list != null) {
            listId = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                listId.add(list.get(i).getProductId());
            }
        }
        if (Constant.BUTTON_TYPE.DETAIL.getValue().equals(typeButton)) {
            handleFacebookPostbackDetail(userId, map.get("text"));
        } else if (Constant.BUTTON_TYPE.ADD.getValue().equals(typeButton)) {
            handleFacebookPostbackAdd(userId, map.get("text"));
        } else if (Constant.BUTTON_TYPE.REMOVE.getValue().equals(typeButton)) {
            handleFacebookPostbackRemove(userId, map.get("text"));
        } else if (Constant.BUTTON_TYPE.SUGGEST.getValue().equals(typeButton)) {
            handleFacebookPostbackSuggest(userId, map.get("text"));
        } else if (Constant.BUTTON_TYPE.OPTIONAL_HISTORY.getValue().equals(typeButton)) {
            showHistory(userId, map.get("timeHistory"), map.get("positionHistory"));
        } else if (Constant.BUTTON_TYPE.TRACKING_HISTORY.getValue().equals(typeButton)) {
            handleAddTrackingHistory(userId, map.get("timeHistory"));
        } else if (Constant.BUTTON_TYPE.OPTIONAL_SHOW.getValue().equals(typeButton)) {
            if (Constant.TYPE_SHOW_CART.equals(map.get("typeShow"))) {
                showProductInCart(userId, listId, map.get("positionShow"));
            } else if (Constant.TYPE_SHOW_SEARCH.equals(map.get("typeShow"))) {
                getProductsMessage(userId, map.get("nameProduct"), map.get("positionShow"));
            } else if (Constant.TYPE_SHOW_PROMOTION.equals(map.get("typeShow"))) {
                showPromotionInSuperMarket(userId, map.get("positionShow"));
            }

        }
        LOG.info("[handleMessageButton] End");
    }


    /**
     * Handle button on menu
     *
     * @param typeMenu
     * @param userId
     */
    private void handleMenuButton(String typeMenu, String userId) {
        LOG.info("[handleMenuButton] Start: typeMenu = " + typeMenu);

        if (Constant.MENU_TYPE.SHOW.getValue().equals(typeMenu)) {
            handleFacebookMenuPostbackShowCart(userId);
        } else if (Constant.MENU_TYPE.RESET.getValue().equals(typeMenu)) {
            handleFacebookMenuPostbackResetCart(userId);
        } else if (Constant.MENU_TYPE.PROMOTION.getValue().equals(typeMenu)) {
            handleFacebookMenuPostbackPromotion(userId);
        } else if (Constant.MENU_TYPE.HISTORY.getValue().equals(typeMenu)) {
            getProductsHistory(userId);
        } else if (Constant.MENU_TYPE.FEEDBACK.getValue().equals(typeMenu)) {
            handleFacebookMenuPostbackFeedback(userId);
        }

        LOG.info("[handleMenuButton] End");
    }


    private void getProductsMessage(String userId, String name, String positionInResult) {
        LOG.info("[getProductsMessage] Start: name = " + name);
        List<Element> elementList = new ArrayList<>();
        //bien kiem tra
        boolean nextShow = false;
        boolean backShow = false;
        int intPositionInResult = Integer.parseInt(positionInResult);
        List<Product> productList = productDao.getProductBySearchName(name, intPositionInResult, Constant.MAX_SHOW_RESULT + Constant.CHECK_NEXT_SEARCH_RESULT);
        List<Product> listRight = new ArrayList<>();
        List<Product> listRightLike = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getName().contains(name) && productList.get(i).getName().indexOf(name) == 0) {
                listRight.add(productList.get(i));
            } else {
                listRightLike.add(productList.get(i));
            }
        }
        productList.clear();
        productList.addAll(listRight);
        productList.addAll(listRightLike);
        int sizeListShowResult = 0;
        if (productList.size() - Constant.MAX_SHOW_RESULT == Constant.CHECK_NEXT_SEARCH_RESULT) {
            nextShow = true;
            sizeListShowResult = Constant.MAX_SHOW_RESULT;
        } else if (productList.size() - Constant.MAX_SHOW_RESULT < Constant.CHECK_NEXT_SEARCH_RESULT) {
            sizeListShowResult = productList.size();
        }

        if (!productList.isEmpty()) {

            for (int i = 0; i < sizeListShowResult; i++) {
                Product product = productList.get(i);
                Element element = new Element();
                element.setImageUrl(product.getImgUrl());
                element.setTitle(product.getName());

                List<Promotion> promotionList = promotionDao.getPromotionByProductId(product.getId());
                StringBuilder strSubtitle = addSubtitleInfo(product, promotionList);
                element.setSubtitle(String.valueOf(strSubtitle));
                List<Button> buttonList = new ArrayList<>();
                buttonList.add(helper.createDetailButton(String.valueOf(product.getId())));
                buttonList.add(helper.createAddButton(String.valueOf(product.getId())));
                element.setButtons(buttonList);
                elementList.add(element);
            }
            facebookAPIService.sendTextMessage(generateReply(userId, elementList));

            List<Button> buttonList = new ArrayList<>();

            if (nextShow) {
                buttonList.add(helper.createMoreShowButton(String.valueOf(intPositionInResult + Constant.MAX_SHOW_RESULT),
                        Constant.TYPE_SHOW_SEARCH, name));
            }
            if (intPositionInResult > 0) {
                buttonList.add(helper.createBackShowButton(String.valueOf(intPositionInResult - Constant.MAX_SHOW_RESULT),
                        Constant.TYPE_SHOW_SEARCH, name));
                backShow = true;
            }
            String optionalMessage = getMessageAfterCheck(nextShow, backShow);
            if (nextShow || backShow) {
                handleSendMessageTextAndButton(userId, buttonList, optionalMessage);
            }
        } else {
            //Xin lỗi bạn, món hàng bạn yêu cầu không có trong siêu thị!
            String text = "\u0058\u0069\u006e \u006c\u1ed7\u0069 \u0062\u1ea1\u006e\u002c \u006d\u00f3\u006e \u0068\u00e0\u006e\u0067 " + "\""
                    + jedisClient.get(userId + "_Say") +
                    "\" " + " \u006b\u0068\u00f4\u006e\u0067 \u0063\u00f3 \u0074\u0072\u006f\u006e\u0067 \u0073\u0069\u00ea\u0075 \u0074\u0068\u1ecb\u0021";
            facebookAPIService.sendTextMessage(userId, text);
        }
        LOG.info("[getProductsMessage] End");

    }

    private boolean showProductInCart(String userId, List<Long> listProductId, String positionShowProductInList) {
        LOG.info("[showProductInCart] Start: userId = " + userId);

        List<Element> elementList = new ArrayList<>();
        List<Product> list;
        List<Long> listProductShow;
        if (!listProductId.isEmpty()) {
            int sizeListProduct = listProductId.size();

            boolean nextShow = false;
            boolean backShow = false;
            int intPositionShowProductInList = Integer.parseInt(positionShowProductInList);
            int maxShow;
            //vị trí cũ của nút xem thêm đã vượt quá size list hiện tại
            if (intPositionShowProductInList > sizeListProduct
                    || intPositionShowProductInList < Constant.BEGIN_SHOW) {
                intPositionShowProductInList = Constant.BEGIN_SHOW;
            }
            if (intPositionShowProductInList + Constant.MAX_SHOW_CART <= sizeListProduct) {
                maxShow = intPositionShowProductInList + Constant.MAX_SHOW_CART;

            } else {
                maxShow = sizeListProduct;

            }
            //
            listProductShow = listProductId.subList(intPositionShowProductInList, maxShow);
            if (listProductShow.isEmpty()) {
                intPositionShowProductInList = Constant.BEGIN_SHOW;
                listProductShow = listProductId.subList(Constant.BEGIN_SHOW, Constant.MAX_SHOW_CART);
            }
            list = productDao.getProductInCart(listProductShow);
            for (Product product : list) {
                Element element = new Element();
                List<Button> buttonListItem = new ArrayList<>();
                element.setTitle(product.getName());

                List<Promotion> promotionList = promotionDao.getSpecificPromotionByProductId(product.getId());
                StringBuilder strSubtitle = addSubtitleInfo(product, promotionList);
                element.setSubtitle(String.valueOf(strSubtitle));

                buttonListItem.add(helper.createDetailButton(String.valueOf(product.getId())));
                buttonListItem.add(helper.createRemoveButton(String.valueOf(product.getId())));
                if (productDao.checkSuggestProductById(product.getId())) {
                    buttonListItem.add(helper.createSuggestButton(String.valueOf(product.getId())));
                }
                element.setButtons(buttonListItem);
                elementList.add(element);
            }

            facebookAPIService.sendTextMessage(generateReply(userId, elementList));

            List<Button> buttonList = new ArrayList<>();
            int checkNext = sizeListProduct - intPositionShowProductInList;
            if (checkNext > Constant.MAX_SHOW_CART) {
                nextShow = true;
                buttonList.add(helper.createMoreShowButton(String.valueOf(intPositionShowProductInList + Constant.MAX_SHOW_CART), Constant.TYPE_SHOW_CART, Constant.STR_BLANK));
            }
            if (intPositionShowProductInList > 0) {
                buttonList.add(helper.createBackShowButton(String.valueOf(intPositionShowProductInList - Constant.MAX_SHOW_CART), Constant.TYPE_SHOW_CART, Constant.STR_BLANK));
                backShow = true;
            }
            String optionalMessage = getMessageAfterCheck(nextShow, backShow);
            if (nextShow || backShow) {
                handleSendMessageTextAndButton(userId, buttonList, optionalMessage);
            }
        } else {
            //Hiện không có món hàng nào trong giỏ, bạn hãy tìm thử cho mình một món hàng nào!
            String text = "\u0048\u0069\u1ec7\u006e \u006b\u0068\u00f4\u006e\u0067 \u0063\u00f3 \u006d\u00f3\u006e \u0068\u00e0\u006e\u0067 \u006e\u00e0\u006f \u0074\u0072\u006f\u006e\u0067 \u0067\u0069\u1ecf\u002c \u0062\u1ea1\u006e \u0068\u00e3\u0079 \u0074\u00ec\u006d \u0074\u0068\u1eed \u0063\u0068\u006f \u006d\u00ec\u006e\u0068 \u006d\u1ed9\u0074 \u006d\u00f3\u006e \u0068\u00e0\u006e\u0067 \u006e\u00e0\u006f\u0021";
            facebookAPIService.sendTextMessage(userId, text);
        }
        LOG.info("[showProductInCart] End");
        return true;
    }


    /**
     * Show promotion
     *
     * @param userId
     */
    private void showPromotionInSuperMarket(String userId, String positionInResult) {
        LOG.info("[showPromotionInSuperMarket] Start: userId = " + userId);
        boolean nextShow = false;
        boolean backShow = false;
        int intPositionInResult = Integer.parseInt(positionInResult);
        List<Element> elementList = new ArrayList<>();

        List<Product> productList = productDao.getPromotionBaseOnWeightHistory(userId);

        int sizeListShowResult = 0;
        if (productList.size() - Constant.MAX_SHOW_RESULT == Constant.CHECK_NEXT_SEARCH_RESULT) {
            nextShow = true;
            sizeListShowResult = Constant.MAX_SHOW_RESULT;
        } else if (productList.size() - Constant.MAX_SHOW_RESULT < Constant.CHECK_NEXT_SEARCH_RESULT) {
            sizeListShowResult = productList.size();
        }

        if (!productList.isEmpty()) {
            Product product;
            for (int i = 0; i < sizeListShowResult; i++) {
                product = productList.get(i);
                List<Promotion> promotionList = promotionDao.getSpecificPromotionByProductId(product.getId());
                Element element = new Element();
                element.setImageUrl(product.getImgUrl());
                element.setTitle(product.getName());

                StringBuilder strSubtitle = addSubtitleInfo(product, promotionList);

                element.setSubtitle(String.valueOf(strSubtitle));
                List<Button> buttonList = new ArrayList<>();
                buttonList.add(helper.createDetailButton(String.valueOf(product.getId())));
                buttonList.add(helper.createAddButton(String.valueOf(product.getId())));
                element.setButtons(buttonList);
                elementList.add(element);
            }
            facebookAPIService.sendTextMessage(generateReply(userId, elementList));

            List<Button> buttonList = new ArrayList<>();

            if (nextShow) {
                buttonList.add(helper.createMoreShowPromotionButton(String.valueOf(intPositionInResult + Constant.MAX_SHOW_RESULT),
                        Constant.TYPE_SHOW_PROMOTION, userId));
            }
            if (intPositionInResult > 0) {
                buttonList.add(helper.createBackShowPromotionButton(String.valueOf(intPositionInResult - Constant.MAX_SHOW_RESULT),
                        Constant.TYPE_SHOW_PROMOTION, userId));
                backShow = true;
            }
            String optionalMessage = getMessageAfterCheck(nextShow, backShow);
            if (nextShow || backShow) {
                handleSendMessageTextAndButton(userId, buttonList, optionalMessage);
            }
        } else {
            String text = "\u0053\u0069\u00ea\u0075 \u0074\u0068\u1ecb \u0111\u0061\u006e\u0067 \u0063\u0068\u0075\u1ea9\u006e \u0062\u1ecb \u0063\u0068\u0075\u1ed7\u0069 \u0073\u1ef1 \u006b\u0069\u1ec7\u006e \u006b\u0068\u0075\u0079\u1ebf\u006e \u006d\u00e3\u0069 \u006d\u1edb\u0069\u002c \u0072\u1ea5\u0074 \u006c\u1ea5\u0079 \u006c\u00e0\u006d \u0074\u0069\u1ebf\u0063 \u0076\u00ec \u006b\u0068\u00f4\u006e\u0067 \u0063\u00f3 \u006b\u0068\u0075\u0079\u1ebf\u006e \u006d\u00e3\u0069 \u0068\u00f4\u006d \u006e\u0061\u0079\u0021";
            facebookAPIService.sendTextMessage(userId, text);
            LOG.info("[showPromotionInSuperMarket] End");
        }
    }

    /**
     * Show history
     *
     * @param userId
     * @param timeHistory
     * @param positionHistory
     */

    private void showHistory(String userId, String timeHistory, String positionHistory) {
        LOG.info("[showHistory] Start: userId = " + userId);
        long millisTime = DateUtil.parseMillisecondFromString(timeHistory, Constant.TYPE_DATE.DAY_MONTH_YEAR_ENOUGH.getValue());
        StringBuilder showData;
        List<Button> buttonList = new ArrayList<>();
        int start = Integer.parseInt(positionHistory);
        if (millisTime != 0) {
            List<History> listHistory
                    = historyDao.getHistoryByTime(userId, millisTime, start, Constant.MAX_SHOW + Constant.CHECK_NEXT_HISTORY);
            if (!listHistory.isEmpty()) {
                //số lượng phần tử trong list
                int sizeListReal;
                if (listHistory.size() > Constant.MAX_SHOW) {
                    sizeListReal = listHistory.size() - Constant.CHECK_NEXT_HISTORY;
                    buttonList.add(helper.createMoreHistoryButton(timeHistory, String.valueOf(start + listHistory.size() - Constant.CHECK_NEXT_HISTORY)));
                } else {
                    sizeListReal = listHistory.size();
                }
                if (start >= Constant.MAX_SHOW) {
                    buttonList.add(helper.createBackHistoryButton(timeHistory, String.valueOf(start - Constant.MAX_SHOW)));
                }

                showData = new StringBuilder("\u004c\u1ecb\u0063\u0068 \u0073\u1eed \u006d\u0075\u0061 \u0068\u00e0\u006e\u0067 \u006e\u0067\u00e0\u0079\u003a " + timeHistory + "\n");
                for (int i = 0; i < sizeListReal; i++) {

                    showData.append(listHistory.get(i).getProductName())
                            .append("\u002c \u0074\u00ec\u006d \u006d\u0075\u0061 \u006c\u00fa\u0063\u003a ")
                            .append(DateUtil.parseDateFromMillisecond(listHistory.get(i).getDate(), "HH:mm:ss"))
                            .append("\n");
                }
                if (listHistory.size() > Constant.MAX_SHOW) {
                    showData.append(Constant.THREE_DOTS);
                }
                if (listHistory.size() <= Constant.MAX_SHOW) {
                    //bạn đã xem toàn bộ lịch sử mua hàng, cám ơn bạn đã quan tâm!
                    showData.append("\n \u0062\u1ea1\u006e \u0111\u00e3 \u0078\u0065\u006d \u0074\u006f\u00e0\u006e \u0062\u1ed9 \u006c\u1ecb\u0063\u0068 \u0073\u1eed \u006d\u0075\u0061 \u0068\u00e0\u006e\u0067\u002c \u0063\u00e1\u006d \u01a1\u006e \u0062\u1ea1\u006e \u0111\u00e3 \u0071\u0075\u0061\u006e \u0074\u00e2\u006d\u0021");
                }
                buttonList.add(helper.createAddHistoryToCartButton(timeHistory));

                handleSendMessageTextAndButton(userId, buttonList, String.valueOf(showData));
            } else {
                //Bạn không có lịch sử mua hàng ngày dd/mm/yyyy
                String text = "\u0042\u1ea1\u006e \u006b\u0068\u00f4\u006e\u0067 \u0063\u00f3 \u006c\u1ecb\u0063\u0068 \u0073\u1eed \u006d\u0075\u0061 \u0068\u00e0\u006e\u0067 \u006e\u0067\u00e0\u0079 "
                        + timeHistory;
                facebookAPIService.sendTextMessage(userId, text);
            }
        } else {
            //Cú pháp để xem lịch sử của bạn là: @ls:dd/mm/yyyy
            //Vui lòng nhập đúng để có kết quả chính xác nhất
            String text = "\u0043\u00fa \u0070\u0068\u00e1\u0070 \u0111\u1ec3 \u0078\u0065\u006d \u006c\u1ecb\u0063\u0068 \u0073\u1eed \u0063\u1ee7\u0061 \u0062\u1ea1\u006e \u006c\u00e0\u003a \u0040\u006c\u0073\u003a\u0064\u0064\u002f\u006d\u006d\u002f\u0079\u0079\u0079\u0079" +
                    "\u0056\u0075\u0069 \u006c\u00f2\u006e\u0067 \u006e\u0068\u1ead\u0070 \u0111\u00fa\u006e\u0067 \u0111\u1ec3 \u0063\u00f3 \u006b\u1ebf\u0074 \u0071\u0075\u1ea3 \u0063\u0068\u00ed\u006e\u0068 \u0078\u00e1\u0063 \u006e\u0068\u1ea5\u0074";
            facebookAPIService.sendTextMessage(userId, text);
        }
        LOG.info("[showHistory] End");
    }

    /**
     * Handle show cart cmd
     *
     * @param userId
     */
    private void handleFacebookMenuPostbackShowCart(String userId) {
        LOG.info("[handleFacebookMenuPostbackShowCart] Start: userId = " + userId);
        List<SaveData> list = (List<SaveData>) jedisClient.get(userId + Constant.CART_POST_FIX);
        List<Long> listId = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            String text = "\u0047\u0069\u1ecf \u0068\u00e0\u006e\u0067 \u0062\u1ea1\u006e \u0111\u0061\u006e\u0067 \u0074\u0072\u1ed1\u006e\u0067";
            facebookAPIService.sendTextMessage(userId, text);
        } else {
            for (int i = 0; i < list.size(); i++) {
                listId.add(list.get(i).getProductId());
            }
            showProductInCart(userId, listId, String.valueOf(Constant.BEGIN_SHOW));
        }
        LOG.info("[handleFacebookMenuPostbackShowCart] End");
    }

    /**
     * Handle reset card cmd
     *
     * @param userId
     */
    private void handleFacebookMenuPostbackResetCart(String userId) {
        LOG.info("[handleFacebookMenuPostbackResetCart] Start: userId = " + userId);

        List<SaveData> list = (List<SaveData>) jedisClient.get(userId + Constant.CART_POST_FIX);

        if (list != null) {
            //send action reset cart to app
            list.clear();
            jedisClient.set(userId + Constant.CART_POST_FIX, list);
            List<SaveData> listProductId = new ArrayList<>();
            sendDataToDeviceByBotFbId(userId, listProductId, Constant.STATUS_CODE.RESET.getValue());
            String text = "\u0110\u00e3 \u006c\u00e0\u006d \u006d\u1edb\u0069 \u0067\u0069\u1ecf \u0068\u00e0\u006e\u0067\u0021";
            facebookAPIService.sendTextMessage(userId, text);
        } else {
            String text = "\u0110\u00e3 \u006c\u00e0\u006d \u006d\u1edb\u0069 \u0067\u0069\u1ecf \u0068\u00e0\u006e\u0067\u0021";
            facebookAPIService.sendTextMessage(userId, text);
        }

        LOG.info("[handleFacebookMenuPostbackResetCart] End");
    }

    /**
     * Handle show promotion cmd
     *
     * @param userId
     */
    private void handleFacebookMenuPostbackPromotion(String userId) {
        LOG.info("[handleFacebookMenuPostbackPromotion] Start: userId = " + userId);
        showPromotionInSuperMarket(userId, String.valueOf(Constant.BEGIN_SHOW));
        LOG.info("[handleFacebookMenuPostbackPromotion] End");
    }

    /**
     * Handle feedback cmd
     *
     * @param userId
     */
    private void handleFacebookMenuPostbackFeedback(String userId) {
        LOG.info("[handleFacebookMenuPostbackFeedback] Start: userId = " + userId);
        String textUserSay = (String) jedisClient.get(userId + "_Say");
        String text;
        if (textUserSay == null) {
            text = "\u0042\u1ea1\u006e \u0111\u00e3 \u0070\u0068\u1ea3\u006e \u0068\u1ed3\u0069 \u0074\u0068\u00e0\u006e\u0068 \u0063\u00f4\u006e\u0067";
        } else {
            ChatLog chatLogEntity = new ChatLog();
            chatLogEntity.setUserSay(textUserSay);
            chatLogEntity.setStatus(Constant.STATUS_NEW);
            chatlogDao.insert(chatLogEntity);
            //Cám ơn bạn đã phản hồi!
            text = "\u0043\u00e1\u006d \u01a1\u006e \u0062\u1ea1\u006e \u0111\u00e3 \u0070\u0068\u1ea3\u006e \u0068\u1ed3\u0069\u0021";
            jedisClient.remove(userId + "_Say");
        }
        facebookAPIService.sendTextMessage(userId, text);

        LOG.info("[handleFacebookMenuPostbackFeedback] End");
    }

    /**
     * Handle show product details cmd
     *
     * @param userId
     * @param productId
     */
    private void handleFacebookPostbackDetail(String userId, String productId) {
        LOG.info("[handleFacebookPostbackDetail] Start: userId = " + userId);
        List<Promotion> listPromotion = promotionDao.getPromotionByProductId(Long.parseLong(productId));
        StringBuilder text = new StringBuilder();
        if (!listPromotion.isEmpty()) {
            //Chương trình khuyến mãi
            text = text.append("\u0043\u0068\u01b0\u01a1\u006e\u0067 \u0074\u0072\u00ec\u006e\u0068 \u006b\u0068\u0075\u0079\u1ebf\u006e \u006d\u00e3\u0069\u003a")
                    .append("\n");
            for (Promotion promotion : listPromotion) {
                text.append(promotion.getDetails());
                //giam
                if (promotion.getDiscountRate() != 0) {
                    text.append(" \u0067\u0069\u1ea3\u006d ")
                            .append(String.valueOf(promotion.getDiscountRate()))
                            .append(Constant.PERCENT);
                }
                text.append(":")
                        .append(Constant.STR_BLANK)
                        .append(DateUtil.parseDateFromMillisecond(promotion.getStartDate(), Constant.TYPE_DATE.DAY_MONTH_YEAR_LIMIT.getValue()))
                        .append("-")
                        .append(DateUtil.parseDateFromMillisecond(promotion.getEndDate(), Constant.TYPE_DATE.DAY_MONTH_YEAR_LIMIT.getValue()))
                        .append("\n ");

            }
        }
        Product product = productDao.getProductById(Long.parseLong(productId));
        //Chi tiết:
        text.append("\u0043\u0068\u0069 \u0074\u0069\u1ebf\u0074\u003a")
                .append("\n")
                .append(product.getDetails());

        facebookAPIService.sendTextMessage(userId, String.valueOf(text));

        LOG.info("[handleFacebookPostbackDetail] End");
    }

    /**
     * Handle add product cmd
     *
     * @param userId
     * @param productId
     */
    private void handleFacebookPostbackAdd(String userId, String productId) {
        LOG.info("[handleFacebookPostbackAdd] Start: userId = " + userId);

        List<SaveData> list = (List<SaveData>) jedisClient.get(userId + Constant.CART_POST_FIX);
        SaveData saveData = new SaveData(Long.valueOf(productId), DateUtil.getCurUTCInMilliseconds());

        if (list == null) {
            list = new ArrayList<>();
        }

        if (!list.contains(saveData)) {

            list.add(saveData);
            jedisClient.set(userId + Constant.CART_POST_FIX, list);
            //send data to app
            List<SaveData> listProductId = new ArrayList<>();
            listProductId.add(saveData);
            sendDataToDeviceByBotFbId(userId, listProductId, Constant.STATUS_CODE.ADD.getValue());

            String text = "\u0110\u00e3 \u0074\u0068\u00ea\u006d \u0076\u00e0\u006f \u0067\u0069\u1ecf \u0068\u00e0\u006e\u0067";
            facebookAPIService.sendTextMessage(userId, text);
        } else {
            String text = "\u0111\u00e3 \u0074\u1ed3\u006e \u0074\u1ea1\u0069 \u0073\u1ea3\u006e \u0070\u0068\u1ea9\u006d \u0074\u0072\u006f\u006e\u0067 \u0067\u0069\u1ecf \u0068\u00e0\u006e\u0067\u0021";
            facebookAPIService.sendTextMessage(userId, text);
        }
        LOG.info("[handleFacebookPostbackAdd] End");
    }

    /**
     * Handle remove product cmd
     *
     * @param userId
     * @param productId
     */
    private void handleFacebookPostbackRemove(String userId, String productId) {
        LOG.info("[handleFacebookPostbackRemove] Start: userId = " + userId);

        List<SaveData> list = (List<SaveData>) jedisClient.get(userId + Constant.CART_POST_FIX);
        List<Long> listId = new ArrayList<>();


        SaveData saveData = new SaveData(Long.valueOf(productId), DateUtil.getCurUTCInMilliseconds());
        if (list.isEmpty()) {
            facebookAPIService.sendTextMessage(userId, "\u0047\u0069\u1ecf \u0068\u00e0\u006e\u0067 \u0111\u0061\u006e\u0067 \u0074\u0072\u1ed1\u006e\u0067");
        } else {
            //bien kiem tra product co ton tai trong list ko
            int position = -1;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getProductId() == Long.valueOf(productId)) {
                    position = i;
                    break;
                }
            }

            if (position != -1) {
                list.remove(position);
                jedisClient.set(userId + Constant.CART_POST_FIX, list);
                //send action remove id to app
                List<SaveData> listProductId = new ArrayList<>();
                listProductId.add(saveData);
                sendDataToDeviceByBotFbId(userId, listProductId, Constant.STATUS_CODE.REMOVE.getValue());
                if (!list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        listId.add(list.get(i).getProductId());
                    }
                    //Content text: Sản phẩm còn lại trong giỏ hàng
                    facebookAPIService.sendTextMessage(userId, "\u0053\u1ea3\u006e \u0070\u0068\u1ea9\u006d \u0063\u00f2\u006e \u006c\u1ea1\u0069 \u0074\u0072\u006f\u006e\u0067 \u0067\u0069\u1ecf \u0068\u00e0\u006e\u0067");
                    //show san pham con lai trong gio hang
                    showProductInCart(userId, listId, String.valueOf(Constant.BEGIN_SHOW));
                } else {
                    //Content text: Giỏ hàng đang trống
                    facebookAPIService.sendTextMessage(userId, "\u0047\u0069\u1ecf \u0068\u00e0\u006e\u0067 \u0111\u0061\u006e\u0067 \u0074\u0072\u1ed1\u006e\u0067");
                }
            } else {
                //Sản phẩm không còn trong giỏ hàng
                facebookAPIService.sendTextMessage(userId, "\u0053\u1ea3\u006e \u0070\u0068\u1ea9\u006d \u006b\u0068\u00f4\u006e\u0067 \u0063\u00f2\u006e \u0074\u0072\u006f\u006e\u0067 \u0067\u0069\u1ecf \u0068\u00e0\u006e\u0067");
            }

        }
        LOG.info("[handleFacebookPostbackRemove] End");
    }


    /**
     * Generate reply
     *
     * @param userId
     * @param elementList
     * @return
     */
    private String generateReply(String userId, List<Element> elementList) {
        LOG.info("[generateReply] Start");

        Payload payload = new Payload();
        payload.setElements(elementList);
        payload.setTemplateType("generic");

        Attachment attachment = new Attachment();
        attachment.setPayload(payload);
        attachment.setType("template");

        Message message = new Message();
        message.setAttachment(attachment);

        Recipient recipient = new Recipient();
        recipient.setId(userId);

        Messaging reply = new Messaging();
        reply.setRecipient(recipient);
        reply.setMessage(message);

        LOG.info("[generateReply] End");
        return new Gson().toJson(reply);
    }

    private void getProductsHistory(String userId) {
        LOG.info("[getProductsHistory] Start: userId = " + userId);
        List<Element> elementList = new ArrayList<>();

        List<String> listDate = historyDao.getDateHistory(userId);
        if (!listDate.isEmpty()) {
            for (String date : listDate) {
                Element element = new Element();

                element.setTitle(date);
                long millisTime = DateUtil.parseMillisecondFromString(date, "dd/MM/yyyy");
                int sumProduct = historyDao.getQuantityProductByTime(userId, millisTime);
                StringBuilder strSubtitle = new StringBuilder("\u0042\u1ea1\u006e \u0111\u00e3 \u006d\u0075\u0061\u003a ")
                        .append(sumProduct)
                        .append(" \u006d\u00f3\u006e \u0068\u00e0\u006e\u0067");

                element.setSubtitle(String.valueOf(strSubtitle));
                String urlPicture = "";
                switch (sumProduct) {
                    case 1:
                        urlPicture = "https://tinyurl.com/zm73o3e";
                        break;
                    case 2:
                        urlPicture = "https://tinyurl.com/jgh39ha";
                        break;
                    case 3:
                        urlPicture = "https://tinyurl.com/z8ycfod";
                        break;
                    default:
                        urlPicture = "https://tinyurl.com/z8ycfod";
                        break;
                }


                element.setImageUrl(urlPicture);
                List<Button> buttonList = new ArrayList<>();
                buttonList.add(helper.createShowDetailHistoryButton(date));
                buttonList.add(helper.createAddHistoryToCartButton(date));
                element.setButtons(buttonList);

                elementList.add(element);
            }
            LOG.info("[getProductsMessage] End");
            facebookAPIService.sendTextMessage(generateReply(userId, elementList));
        } else {
            //Hiện bạn là thành viên mới, tìm mua thành công món hàng quay lại xem lịch sử sau nhé!
            String text = "\u0048\u0069\u1ec7\u006e \u0062\u1ea1\u006e \u006c\u00e0 \u0074\u0068\u00e0\u006e\u0068 \u0076\u0069\u00ea\u006e \u006d\u1edb\u0069\u002c \u0074\u00ec\u006d \u006d\u0075\u0061 \u0074\u0068\u00e0\u006e\u0068 \u0063\u00f4\u006e\u0067 \u006d\u00f3\u006e \u0068\u00e0\u006e\u0067 \u0071\u0075\u0061\u0079 \u006c\u1ea1\u0069 \u0078\u0065\u006d \u006c\u1ecb\u0063\u0068 \u0073\u1eed \u0073\u0061\u0075 \u006e\u0068\u00e9\u0021";
            LOG.info("[getProductsHistory] End");
            facebookAPIService.sendTextMessage(userId, text);
        }
    }

    private void handleAddTrackingHistory(String userId, String date) {
        LOG.info("[handleAddTrackingHistory] Start: userId = " + userId);
        List<SaveData> list = (List<SaveData>) jedisClient.get(userId + Constant.CART_POST_FIX);

        if (list == null) {
            list = new ArrayList<>();
        }
        long millisTime = DateUtil.parseMillisecondFromString(date, "dd/MM/yyyy");
        List<Long> listProductId = historyDao.getProductIdInSpecificDay(userId, millisTime);
        for (Long productId : listProductId) {
            if (!list.contains(productId)) {
                SaveData saveData = new SaveData(productId, DateUtil.getCurUTCInMilliseconds());
                list.add(saveData);
            }
        }
        //send action clone history to app
        sendDataToDeviceByBotFbId(userId, list, Constant.STATUS_CODE.CLONE.getValue());
        jedisClient.set(userId + Constant.CART_POST_FIX, list);
        String text = "\u0110\u00e3 \u0074\u0068\u00ea\u006d \u0063\u00e1\u0063 \u0073\u1ea3\u006e \u0070\u0068\u1ea9\u006d \u006e\u0067\u00e0\u0079 "
                + date + " \u0076\u00e0\u006f \u0067\u0069\u1ecf \u0068\u00e0\u006e\u0067";
        facebookAPIService.sendTextMessage(userId, text);
        LOG.info("[handleAddTrackingHistory] End");
    }

    private void handleSendMessageTextAndButton(String userId, List<Button> buttonList, String optionalMessage) {
        LOG.info("[handleSendMessageTextAndButton] Start: userId = " + userId);
        Payload payload = new Payload();
        //kieu text va nut
        payload.setTemplateType("button");
        payload.setText(optionalMessage);
        payload.setButtons(buttonList);
        Attachment attachment = new Attachment();
        attachment.setPayload(payload);
        attachment.setType("template");

        Message message = new Message();
        message.setAttachment(attachment);

        Recipient recipient = new Recipient();
        recipient.setId(userId);
        Messaging reply = new Messaging();
        reply.setRecipient(recipient);
        reply.setMessage(message);
        facebookAPIService.sendTextMessage(new Gson().toJson(reply));
        LOG.info("[handleSendMessageTextAndButton] End");
    }

    private String getMessageAfterCheck(boolean nextShow, boolean backShow) {
        LOG.info("[getMessageAfterCheck] Start:");
        String optionalMessage = Constant.STR_BLANK;
        if (nextShow && backShow) {
            //Bạn có muốn quay lại trước hay xem tiếp món hàng :D
            optionalMessage = "\u0042\u1ea1\u006e \u0063\u00f3 \u006d\u0075\u1ed1\u006e \u0074\u0069\u1ebf\u0070 \u0074\u1ee5\u0063 \u0078\u0065\u006d \u0063\u00e1\u0063 \u0063\u00e1\u0063 \u006d\u00f3\u006e \u0068\u00e0\u006e\u0067 \u006b\u0068\u00f4\u006e\u0067 \u003a\u0044";
        } else {
            if (nextShow) {
                //"Bạn có muốn xem thêm các món hàng không :D"
                optionalMessage = "\u0042\u1ea1\u006e \u0063\u00f3 \u006d\u0075\u1ed1\u006e \u0078\u0065\u006d \u0074\u0068\u00ea\u006d \u0063\u00e1\u0063 \u006d\u00f3\u006e \u0068\u00e0\u006e\u0067 \u006b\u0068\u00f4\u006e\u0067 \u003a\u0044";
            } else if (backShow) {
                //Bạn có muốn xem lại các món hàng trước không :D
                optionalMessage = "\u0042\u1ea1\u006e \u0063\u00f3 \u006d\u0075\u1ed1\u006e \u0078\u0065\u006d \u006c\u1ea1\u0069 \u0063\u00e1\u0063 \u006d\u00f3\u006e \u0068\u00e0\u006e\u0067 \u0074\u0072\u01b0\u1edb\u0063 \u006b\u0068\u00f4\u006e\u0067 \u003a\u0044";

            }
        }
        LOG.info("[getMessageAfterCheck] End");
        return optionalMessage;
    }

    private StringBuilder addSubtitleInfo(Product product, List<Promotion> promotionList) {
        LOG.info("[addSubtitleInfo] Start:");
        StringBuilder strSubtitle = new StringBuilder(String.valueOf(
                NumberFormat.getNumberInstance(Locale.US).format(product.getPrice())))
                .append(" " + Constant.TYPE_MONEY)
                .append(" \n")
                .append(String.valueOf(product.getAreaLocation()))
                .append(" \n")
                .append(String.valueOf(product.getAreaName()))
                .append(".\n");

        if (promotionList.size() > 0) {
            Promotion promotion = promotionList.get(promotionList.size() - 1);

            String details = promotion.getDetails();
            if (promotion.getDiscountRate() != 0) {
                String discountRate = String.valueOf(promotion.getDiscountRate());
                strSubtitle
                        //Giảm:
                        .append(" \u0047\u0069\u1ea3\u006d\u003a ")
                        .append(discountRate)
                        .append(Constant.PERCENT + " \n");
            } else {
                strSubtitle.append(details);
            }
        }
        LOG.info("[addSubtitleInfo] End");
        return strSubtitle;
    }

    private void sendDataToDeviceByBotFbId(String userId, List<SaveData> listProductId, int statusCode) {
        LOG.info("[sendDataToDeviceByBotFbId] Start: userId " + userId);
        ProductResponse productResponse = new ProductResponse(listProductId, statusCode);
        Gson gson = new Gson();
        String jSonproductId = gson.toJson(productResponse);
        List<String> listToken = deviceTokenDao.getDeviceTokenByBotFbId(userId);
        for (String token : listToken) {
            fcmService.sendDataMessage(jSonproductId, token);
        }
        LOG.info("[sendDataToDeviceByBotFbId] End");
    }

    private void handleFacebookPostbackSuggest(String userId, String productId) {
        LOG.info("[handleFacebookPostbackSuggest] Start: userId = " + userId);
        List<Element> elementList = new ArrayList<>();
        List<Product> productList = productDao.getSuggestProductById(Long.parseLong(productId));
        if (!productList.isEmpty()) {

            for (int i = 0; i < productList.size(); i++) {
                Product product = productList.get(i);
                Element element = new Element();
                element.setImageUrl(product.getImgUrl());
                element.setTitle(product.getName());

                List<Promotion> promotionList = promotionDao.getPromotionByProductId(product.getId());
                StringBuilder strSubtitle = addSubtitleInfo(product, promotionList);
                element.setSubtitle(String.valueOf(strSubtitle));
                List<Button> buttonList = new ArrayList<>();
                buttonList.add(helper.createDetailButton(String.valueOf(product.getId())));
                buttonList.add(helper.createAddButton(String.valueOf(product.getId())));
                element.setButtons(buttonList);
                elementList.add(element);
            }
            facebookAPIService.sendTextMessage(generateReply(userId, elementList));

            LOG.info("[handleFacebookPostbackSuggest] End");
        } else {
            //Đây là món hàng mới của siêu thị, sẽ được cập nhật các món hàng gợi ý trong tuơng lai!
            String text = "\u0110\u00e2\u0079 \u006c\u00e0 \u006d\u00f3\u006e \u0068\u00e0\u006e\u0067 \u006d\u1edb\u0069 \u0063\u1ee7\u0061 \u0073\u0069\u00ea\u0075 \u0074\u0068\u1ecb\u002c \u0073\u1ebd \u0111\u01b0\u1ee3\u0063 \u0063\u1ead\u0070 \u006e\u0068\u1ead\u0074 \u0063\u00e1\u0063 \u006d\u00f3\u006e \u0068\u00e0\u006e\u0067 \u0067\u1ee3\u0069 \u00fd \u0074\u0072\u006f\u006e\u0067 \u0074\u0075\u01a1\u006e\u0067 \u006c\u0061\u0069\u0021 ";
            facebookAPIService.sendTextMessage(userId, text);
        }
    }
}
