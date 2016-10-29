package com.fu.vision.service;

import com.google.api.services.vision.v1.model.EntityAnnotation;

import java.util.List;

/**
 * Created by manlm on 10/27/2016.
 */
public interface VisionService {

    List<EntityAnnotation> detectText(byte[] data, int maxResults);

    List<EntityAnnotation> detectLogo(byte[] data, int maxResults);
}
