package com.fu.database.dao.impl;

import com.fu.database.dao.RoleDao;
import com.fu.database.entity.Role;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

/**
 * Created by manlm on 7/24/2016.
 */
@Repository
public class RoleDaoImpl extends GenericDaoImpl<Role, Integer> implements RoleDao {

    private static final Logger LOG = Logger.getLogger(RoleDaoImpl.class);
}
