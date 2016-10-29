package com.fu.vision.service.impl;

import com.fu.vision.service.VisionService;
import com.fu.vision.utils.Constant;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.*;
import com.google.common.collect.ImmutableList;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by manlm on 10/27/2016.
 */
@Service
public class VisionServiceImpl implements VisionService {

    private static final String APPLICATION_NAME = "ISMB";

    private static final Logger LOG = Logger.getLogger(VisionServiceImpl.class);

    private final Vision vision;

    public VisionServiceImpl() {
        this.vision = getVisionService();
    }

    /**
     * Connects to the Vision API using Application Default Credentials.
     */
    private Vision getVisionService() {
        LOG.info("[getVisionService] Start");

        GoogleCredential credential;
        JsonFactory jsonFactory;
        try {
            credential = GoogleCredential.getApplicationDefault().createScoped(VisionScopes.all());
            jsonFactory = JacksonFactory.getDefaultInstance();

            LOG.info("[getVisionService] End");
            return new Vision.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (IOException e) {
            LOG.info("[getVisionService] IOException: " + e);
            return null;
        } catch (GeneralSecurityException e) {
            LOG.info("[getVisionService] GeneralSecurityException: " + e);
            return null;
        }
    }

    /**
     * Gets up to {@code maxResults} faces for an image stored at {@code path}.
     */
    @Override
    public List<EntityAnnotation> detectText(byte[] data, int maxResults) {
        try {
            List<String> languages = new ArrayList<>();
            languages.add("vi");
            languages.add("en");

            ImageContext imageContext = new ImageContext();
            imageContext.setLanguageHints(languages);

            AnnotateImageRequest request =
                    new AnnotateImageRequest()
                            .setImage(new Image().encodeContent(data))
                            .setImageContext(imageContext)
                            .setFeatures(ImmutableList.of(
                                    new Feature()
                                            .setType(Constant.VISION_TYPE.TEXT_DETECTION.getValue())
//                                            .setMaxResults(maxResults)
                            ));
            Vision.Images.Annotate annotate =
                    vision.images()
                            .annotate(new BatchAnnotateImagesRequest().setRequests(ImmutableList.of(request)));
            // Due to a bug: requests to Vision API containing large images fail when GZipped.
            annotate.setDisableGZipContent(true);

            BatchAnnotateImagesResponse batchResponse = annotate.execute();
            assert batchResponse.getResponses().size() == 1;
            AnnotateImageResponse response = batchResponse.getResponses().get(0);
            if (response.getTextAnnotations() == null) {
                throw new IOException(
                        response.getError() != null
                                ? response.getError().getMessage()
                                : "Unknown error getting image annotations");
            }
            return response.getTextAnnotations();
        } catch (IOException e) {
            LOG.error("[detectText] IOException: " + e);
            return new ArrayList<>();
        }
    }

    /**
     * Gets up to {@code maxResults} faces for an image stored at {@code path}.
     */
    @Override
    public List<EntityAnnotation> detectLogo(byte[] data, int maxResults) {
        try {
            List<String> languages = new ArrayList<>();
            languages.add("vi");
            languages.add("en");

            ImageContext imageContext = new ImageContext();
            imageContext.setLanguageHints(languages);

            AnnotateImageRequest request =
                    new AnnotateImageRequest()
                            .setImage(new Image().encodeContent(data))
                            .setImageContext(imageContext)
                            .setFeatures(ImmutableList.of(
                                    new Feature()
                                            .setType(Constant.VISION_TYPE.LOGO_DETECTION.getValue())
//                                            .setMaxResults(maxResults)
                            ));
            Vision.Images.Annotate annotate =
                    vision.images()
                            .annotate(new BatchAnnotateImagesRequest().setRequests(ImmutableList.of(request)));
            // Due to a bug: requests to Vision API containing large images fail when GZipped.
            annotate.setDisableGZipContent(true);

            BatchAnnotateImagesResponse batchResponse = annotate.execute();
            assert batchResponse.getResponses().size() == 1;
            AnnotateImageResponse response = batchResponse.getResponses().get(0);
            if (response.getLogoAnnotations() == null) {
                throw new IOException(
                        response.getError() != null
                                ? response.getError().getMessage()
                                : "Unknown error getting image annotations");
            }
            return response.getLogoAnnotations();
        } catch (IOException e) {
            LOG.error("[detectText] IOException: " + e);
            return new ArrayList<>();
        }
    }
}
