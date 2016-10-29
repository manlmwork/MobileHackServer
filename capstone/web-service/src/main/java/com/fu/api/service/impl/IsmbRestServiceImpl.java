package com.fu.api.service.impl;

import com.fu.api.model.*;
import com.fu.api.service.IsmbRestService;
import com.fu.bot.model.ChatMessage;
import com.fu.bot.model.ProductResponse;
import com.fu.bot.model.SaveData;
import com.fu.bot.utils.Constant;
import com.fu.cache.client.JedisClient;
import com.fu.common.util.DateUtil;
import com.fu.database.dao.*;
import com.fu.database.entity.Customer;
import com.fu.database.entity.DeviceToken;
import com.fu.database.entity.History;
import com.fu.database.entity.Product;
import com.fu.storage.service.S3Service;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by manlm on 10/1/2016.
 */
@Service
public class IsmbRestServiceImpl implements IsmbRestService {

    private static final Logger LOG = Logger.getLogger(IsmbRestServiceImpl.class);

    private final JedisClient jedisClient;

    private final DeviceTokenDao deviceTokenDao;

    private final CustomerDao customerDao;

    private final ProductDao productDao;

    private final BeaconDao beaconDao;

    private final AreaDao areaDao;

    private final HistoryDao historyDao;

    private final S3Service s3Service;

    @Autowired
    public IsmbRestServiceImpl(JedisClient jedisClient, DeviceTokenDao deviceTokenDao, CustomerDao customerDao, ProductDao productDao, BeaconDao beaconDao, AreaDao areaDao, HistoryDao historyDao, S3Service s3Service) {
        this.jedisClient = jedisClient;
        this.deviceTokenDao = deviceTokenDao;
        this.customerDao = customerDao;
        this.productDao = productDao;
        this.beaconDao = beaconDao;
        this.areaDao = areaDao;
        this.historyDao = historyDao;
        this.s3Service = s3Service;
    }

    @Override
    public boolean authenticate(String token) {
        LOG.info("[authenticate] Start");
        if (deviceTokenDao.getById(token) == null) {
            LOG.info("[authenticate] End");
            return false;
        }
        LOG.info("[authenticate] End");
        return true;
    }

    @Override
    public String registerPhone(String deviceToken, PhoneInfo phoneInfo) {
        LOG.info("[registerPhone] Start");

        Customer customer
                = customerDao.getByPhone(phoneInfo.getPhone());

        if (customer == null) {
            customer = new Customer();
            customer.setBotFbId("");
            customer.setAppFbId(phoneInfo.getFbId());
            customer.setPhone(phoneInfo.getPhone());
            customer.setFirstName(phoneInfo.getFirstName());
            customer.setLastName(phoneInfo.getLastName());
            customer = customerDao.insert(customer);
        } else {

            if ("".equals(customer.getAppFbId())) {
                customer.setAppFbId(phoneInfo.getFbId());
                customer = customerDao.update(customer);
            } else if (!customer.getAppFbId().equals(phoneInfo.getFbId())) {
                customer.setAppFbId(phoneInfo.getFbId());
                customer = customerDao.insert(customer);
            }

        }

        DeviceToken token = new DeviceToken();
        token.setCustomerAccontId(customer.getId());
        token.setBotFbId(customer.getBotFbId());
        token.setAppFbId(customer.getAppFbId());
        token.setToken(deviceToken);

        if (deviceTokenDao.getById(deviceToken) == null) {
            deviceTokenDao.insert(token);
        }

        LOG.info("[registerPhone] End");
        return customer.getBotFbId();
    }

    @Override
    public List<SaveData> getCart(String botFbId) {
        LOG.info("[getCart] Start");
        LOG.info("[getCart] End");
        return (List<SaveData>) jedisClient.get(botFbId + Constant.CART_POST_FIX);
    }

    @Override
    public void syncCart(CartInfo cartInfo) {
        LOG.info("[syncCart] Start");
        List<SaveData> productListCart = (List<SaveData>) jedisClient.get(cartInfo.getBotFbId() + Constant.CART_POST_FIX);
        if (productListCart != null) {
            List<Cart> cart = cartInfo.getCart();
            Iterator<SaveData> iterator = productListCart.iterator();

            String botFbId = cartInfo.getBotFbId();
            History history;
            SaveData saveData;
            Product product;
            Customer customer;

            for (Cart aCart : cart) {
                while (iterator.hasNext()) {
                    saveData = iterator.next();
                    if (aCart.getProductId() == saveData.getProductId()) {
                        if ((aCart.getLastUpdate() > saveData.getTimeHandle())) {
                            product = productDao.getProductById(saveData.getProductId());
                            customer = customerDao.getByBotFBId(botFbId);
                            history = new History(product.getId(), product.getName()
                                    , customer.getId(), customer.getBotFbId(), customer.getAppFbId(),
                                    saveData.getTimeHandle());
                            historyDao.insert(history);
                            iterator.remove();
                        }
                    }
                }
            }
            List<SaveData> result = new ArrayList();
            while (iterator.hasNext()) {
                result.add(iterator.next());
            }
            if (result.isEmpty()) {
                jedisClient.remove(cartInfo.getBotFbId() + Constant.CART_POST_FIX);
            } else {
                jedisClient.set(cartInfo.getBotFbId() + Constant.CART_POST_FIX, result);
            }
        }
        LOG.info("[syncCart] End");
    }

    @Override
    public ProductModel getProduct() {
        LOG.info("[getProduct] Start");
        ProductModel productModel = new ProductModel();
        productModel.setProductList(productDao.getProductForApi());
        productModel.setLastSync(DateUtil.getCurUTCInMilliseconds());
        LOG.info("[getProduct] End");
        return productModel;
    }

    @Override
    public BeaconModel getBeacon() {
        LOG.info("[getBeacon] Start");
        BeaconModel beaconModel = new BeaconModel();
        beaconModel.setBeaconList(beaconDao.getBeaconForApi());
        LOG.info("[getBeacon] End");
        return beaconModel;
    }

    @Override
    public AreaModel getArea() {
        LOG.info("[getArea] Start");
        AreaModel areaModel = new AreaModel();
        areaModel.setAreaList(areaDao.getAll());
        areaModel.setLastSync(DateUtil.getCurUTCInMilliseconds());
        LOG.info("[getArea] End");
        return areaModel;
    }

    @Override
    public String saveImg(ChatMessage chatMessage) {
        chatMessage.getMess().getImage();
        String fileName = String.valueOf(DateUtil.getCurUTCInMilliseconds());

        return s3Service.uploadObject("s3://manlm/" + fileName, chatMessage.getMess().getImage(), fileName);
    }


}