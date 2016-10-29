package com.fu.database.dao.impl;

import com.fu.database.dao.AnalizedChatLogDao;
import com.fu.database.entity.AnalizedChatLog;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class AnalizedChatLogDaoImpl extends GenericDaoImpl<AnalizedChatLog, Long> implements AnalizedChatLogDao{

    private static final Logger LOG = Logger.getLogger(AnalizedChatLogDaoImpl.class);
}
