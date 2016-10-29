package com.fu.api.model;

import com.fu.database.entity.Area;


import java.util.List;

/**
 * Created by PhucNT on 10/20/2016.
 */

public class AreaModel {

    private List<Area> areaList;

    private long lastSync;

    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }

    public long getLastSync() {
        return lastSync;
    }

    public void setLastSync(long lastSync) {
        this.lastSync = lastSync;
    }
}
