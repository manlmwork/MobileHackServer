package com.fu.storage.service.impl;

import com.fu.storage.service.S3Service;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by manlm on 8/15/2016.
 */
@Service
public class S3ServiceImpl implements S3Service {

    private static final Logger LOG = Logger.getLogger(S3ServiceImpl.class);

    private final ResourceLoader resourceLoader;

    /**
     * Constructor
     *
     * @param resourceLoader
     */
    @Autowired
    public S3ServiceImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Down load file from S3
     *
     * @param location s3://bucketName/fileName
     * @return
     */
    @Override
    public String getObject(String location) {
        LOG.info("[getObject] Start: location = " + location);
        Resource resource = this.resourceLoader.getResource(location);
        try {
            InputStream inputStream = resource.getInputStream();
            LOG.info("[getObject] End");
            return IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            LOG.error("[getObject] IOException: " + e);
            return null;
        }
    }

    /**
     * Upload object to S3
     *
     * @param location s3://bucketName/fileName
     * @param obj
     * @return
     */
    @Override
    public String uploadObject(String location, byte[] obj, String fileName) {
        LOG.info("[uploadObject] Start: location = " + location);
        Resource resource = this.resourceLoader.getResource(location);
        WritableResource writableResource = (WritableResource) resource;
        try (OutputStream outputStream = writableResource.getOutputStream()) {
            outputStream.write(obj);
            LOG.info("[uploadObject] End");
            return "https://manlm.s3.amazonaws.com/"+fileName;
        } catch (IOException e) {
            LOG.error("[uploadObject] IOException: " + e);
            return null;
        }
    }
}
