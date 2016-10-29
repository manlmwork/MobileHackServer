package com.fu.vision.utils;

/**
 * Created by manlm on 10/27/2016.
 */
public class Constant {

    public enum VISION_TYPE {
        LABEL_DETECTION("LABEL_DETECTION"), TEXT_DETECTION("TEXT_DETECTION"), FACE_DETECTION("FACE_DETECTION"), LANDMARK_DETECTION("LANDMARK_DETECTION"), LOGO_DETECTION("LOGO_DETECTION"), SAFE_SEARCH_DETECTION("SAFE_SEARCH_DETECTION"), IMAGE_PROPERTIES("IMAGE_PROPERTIES");

        private String value;

        VISION_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
