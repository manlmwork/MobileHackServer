package com.fu.database.dao.impl;

import com.fu.database.dao.CustomerDao;
import com.fu.database.entity.Customer;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

/**
 * Created by manlm on 9/21/2016.
 */
@Repository
public class CustomerDaoImpl extends GenericDaoImpl<Customer, Long> implements CustomerDao {

    private static final Logger LOG = Logger.getLogger(CustomerDaoImpl.class);

    /**
     * Check if fb account already registered
     *
     * @param fbId
     * @return
     */
    @Override
    public boolean isExist(String fbId) {
        LOG.info("[isExist] Start");
        LOG.info("[isExist] End");
        return !getEntityManager()
                .createQuery("SELECT f.id FROM " + Customer.class.getSimpleName() + " f WHERE f.botFbId = :fbId")
                .setParameter("fbId", fbId)
                .getResultList().isEmpty();
    }

    /**
     * Insert phone knowing fb id on chatbot
     *
     * @param botFbId
     * @param phone
     * @return
     */
    @Override
    public int insertPhoneByBotFbId(String botFbId, String phone) {
        LOG.info("[insertPhoneByBotFbId] Start");
        LOG.info("[insertPhoneByBotFbId] End");
        return getEntityManager().createQuery("UPDATE " + Customer.class.getSimpleName()
                + " c SET c.phone = :phone WHERE c.botFbId = :botFbId")
                .setParameter("phone", phone)
                .setParameter("botFbId", botFbId)
                .executeUpdate();
    }

    /**
     * Get BotFbId By Phone
     *
     * @param phone
     * @return
     */
    @Override
    public Customer getByPhone(String phone) {
        LOG.info("[getBotFbIdByPhone] Start");
        try {
            LOG.info("[getBotFbIdByPhone] End");
            return (Customer) getEntityManager().createQuery("FROM "
                    + Customer.class.getSimpleName()
                    + " c WHERE c.phone = :phone")
                    .setParameter("phone", phone)
                    .getSingleResult();
        } catch (NoResultException e) {
            LOG.error("[getBotFbIdByPhone] NoResultException: " + e);
            return null;
        }
    }

    @Override
    public Customer getByBotFBId(String botFbId) {
        LOG.info("[getByBotFBId] Start");
        try {
            LOG.info("[getByBotFBId] End");
            return (Customer) getEntityManager()
                    .createQuery("FROM " + Customer.class.getSimpleName() + " f WHERE f.botFbId = :botFbId")
                    .setParameter("botFbId", botFbId)
                    .getSingleResult();
        } catch (NoResultException e) {
            LOG.error("[getByBotFBId] NoResultException: " + e);
            return null;
        }
    }
}
