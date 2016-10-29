package com.fu.database.dao;

import com.fu.database.entity.Area;

import java.util.List;

/**
 * Created by manlm on 9/21/2016.
 */
public interface AreaDao extends GenericDao<Area, Integer> {

    List<Area> getAll();
}
