package com.fu.database.dao.impl;

import com.fu.database.dao.ChatLogDao;
import com.fu.database.entity.ChatLog;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

/**
 * Created by manlm on 9/21/2016.
 */
@Repository
public class ChatLogDaoImpl extends GenericDaoImpl<ChatLog, Long> implements ChatLogDao {

    private static final Logger LOG = Logger.getLogger(ChatLogDaoImpl.class);
}
