package com.fu.cache.client;

import com.fu.common.util.CommonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

@Service
public class JedisClient {

    private static final Logger LOG = Logger.getLogger(JedisClient.class);

    private static final String JEDIS_CONNECTION_EXCEPTION = "JedisConnectionException";

    private final JedisPool pool;

    /**
     * Constructor
     *
     * @param pool
     */
    @Autowired
    public JedisClient(JedisPool pool) {
        this.pool = pool;
    }

    /**
     * Get cache
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object get(String key) {
        LOG.info("[get] Start: key = " + key);
        Jedis client = null;
        try {
            client = pool.getResource();
            if (client.get(key) == null) {
                LOG.info("[get] End");
                return null;
            }
            LOG.info("[get] End");
            return CommonUtil.byteStringToOjb(client.get(key));
        } catch (JedisConnectionException e) {
            LOG.error("[get] " + JEDIS_CONNECTION_EXCEPTION + ": " + e);
            return null;
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    /**
     * Set cache
     *
     * @param key
     * @param value
     * @param <E>
     * @return
     */
    public <E> String set(String key, E value) {
        LOG.info("[set] Start: key = " + key);
        Jedis client = null;
        try {
            client = pool.getResource();
            LOG.info("[set] End");
            return client.set(key, CommonUtil.ojbToByteString(value));
        } catch (JedisConnectionException e) {
            LOG.error("[set] " + JEDIS_CONNECTION_EXCEPTION + ": " + e);
            return null;
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    /**
     * Set cache
     *
     * @param key
     * @param value
     * @param expireTime in second
     * @param <E>
     * @return
     */
    public <E> String set(String key, E value, int expireTime) {
        LOG.info("[set] Start: key = " + key);
        Jedis client = null;
        try {
            client = pool.getResource();
            LOG.info("[set] End");
            String result = client.set(key, CommonUtil.ojbToByteString(value));
            client.expire(key, expireTime);
            return result;
        } catch (JedisConnectionException e) {
            LOG.error("[set] " + JEDIS_CONNECTION_EXCEPTION + ": " + e);
            return null;
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    /**
     * Remove cache
     *
     * @param key
     * @return number of keys that were removed
     */
    public long remove(String key) {
        LOG.info("[remove] Start: key = " + key);
        Jedis client = null;
        try {
            client = pool.getResource();
            LOG.info("[remove] End");
            return client.del(key);
        } catch (JedisConnectionException e) {
            LOG.error("[remove] " + JEDIS_CONNECTION_EXCEPTION + ": " + e);
            return 0;
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    /**
     * Check key exist
     *
     * @param key
     * @return
     */
    public boolean isExist(String key) {
        LOG.info("[isExist] Start: key = " + key);
        Jedis client = null;
        try {
            client = pool.getResource();
            LOG.info("[isExist] End");
            return client.exists(key);
        } catch (JedisConnectionException e) {
            LOG.error("[isExist] " + JEDIS_CONNECTION_EXCEPTION + ": " + e);
            return false;
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }
}
