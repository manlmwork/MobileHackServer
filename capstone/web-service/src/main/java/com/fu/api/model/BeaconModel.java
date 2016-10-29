package com.fu.api.model;

import com.fu.database.entity.Beacon;
import com.fu.database.model.BeaconApi;


import java.util.List;

/**
 * Created by PhucNT on 10/20/2016.
 */

public class BeaconModel {

    private List<BeaconApi> beaconList;

    private long lastSync;

    public List<BeaconApi> getBeaconList() {
        return beaconList;
    }

    public void setBeaconList(List<BeaconApi> beaconList) {
        this.beaconList = beaconList;
    }

    public long getLastSync() {
        return lastSync;
    }

    public void setLastSync(long lastSync) {
        this.lastSync = lastSync;
    }
}
