package com.fu.api.model;

/**
 * Created by manlm on 10/1/2016.
 */
public class BeaconInfo {

    private String fbId;

    private int major;

    private int minor;

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }
}
