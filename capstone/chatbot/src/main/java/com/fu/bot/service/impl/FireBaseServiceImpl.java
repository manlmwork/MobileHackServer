package com.fu.bot.service.impl;

import com.fu.bot.service.AccentizerService;
import com.fu.bot.service.FireBaseService;
import com.fu.bot.utils.FirebaseUtils;
import com.fu.database.dao.ProductDao;
import com.fu.database.entity.Product;
import com.fu.nlp.service.NaturalLanguageProcessingService;
import com.fu.vision.service.VisionService;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.aspectj.bridge.Version.text;

@Service
public class FireBaseServiceImpl implements FireBaseService {

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
        List<Product> list;
        List<EntityAnnotation> logos = visionService.detectLogo(imageData, 0);
        String responseText = logos.get(0).getDescription();
        list = productDao.getProductBySearchName(responseText, 0, 10);

        return list;
    }

    public List<Product> handleTextFromUser(String textMessage) {
        LOG.info("[handleTextFromUser] Start ");
        // normal text
        String responseText = naturalLanguageProcessingService.processSpeech(accentizerService.add(text));
        List<Product> list = productDao.getProductBySearchName(responseText, 0, 10);

        LOG.info("[handleTextFromUser] End ");
        return list;

    }
}
