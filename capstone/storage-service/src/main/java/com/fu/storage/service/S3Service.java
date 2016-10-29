package com.fu.storage.service;

/**
 * Created by manlm on 8/15/2016.
 */
public interface S3Service {

    /**
     * Download Object from S3
     *
     * @param location
     * @return
     */
    String getObject(String location);

    /**
     * Upload Object to S3
     *
     * @param location
     * @param obj
     * @return
     */
    String uploadObject(String location, byte[] obj, String fileName);
}
