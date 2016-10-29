package com.fu.database.dao.impl;

import com.fu.database.dao.BeaconDao;
import com.fu.database.entity.Beacon;
import com.fu.database.model.BeaconApi;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manlm on 9/21/2016.
 */
@Repository
public class BeaconDaoImpl extends GenericDaoImpl<Beacon, Integer> implements BeaconDao {

    private static final Logger LOG = Logger.getLogger(BeaconDaoImpl.class);

    @Override
    public List<BeaconApi> getBeaconForApi() {
        LOG.info("[getBeaconForApi] Start");

        List<Beacon> beaconList = getEntityManager().createQuery("SELECT b FROM Beacon b", Beacon.class)
                .getResultList();

        List<BeaconApi> beaconApiList = new ArrayList<>();
        BeaconApi beaconApi;
        for (Beacon beacon : beaconList) {
            beaconApi = new BeaconApi();
            beaconApi.setId(beacon.getId());
            beaconApi.setUuid(beacon.getUuid());
            beaconApi.setMajor(beacon.getMajor());
            beaconApi.setMinor(beacon.getMinor());
            beaconApi.setX(beacon.getX());
            beaconApi.setY(beacon.getY());
            beaconApi.setZ(beacon.getZ());
            beaconApiList.add(beaconApi);
        }

        LOG.info("[getBeaconForApi] End");
        return beaconApiList;
    }
}
