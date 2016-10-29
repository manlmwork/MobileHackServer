package com.fu.database.config;

import com.fu.common.constant.KeyConstant;
import com.fu.common.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

public class MyLocalEntityManagerFactoryBean extends LocalEntityManagerFactoryBean implements Serializable {

    private final Properties properties;

    /**
     * Constructor
     *
     * @param properties
     */
    @Autowired
    public MyLocalEntityManagerFactoryBean(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Map<String, Object> getJpaPropertyMap() {
        String key = properties.getProperty("aes.key") + KeyConstant.AES_KEY;

        Map<String, Object> jpaPropertyMap = super.getJpaPropertyMap();
        Properties hibernateProperties = new Properties();

        String url = AESUtil.decryptByAES(properties.getProperty("jdbc.url"), key);
        String username = AESUtil.decryptByAES(properties.getProperty("jdbc.user"), key);
        String password = AESUtil.decryptByAES(properties.getProperty("jdbc.pass"), key);

        hibernateProperties.setProperty("hibernate.connection.driver_class"
                , properties.getProperty("jdbc.driverClassName"));
        hibernateProperties.setProperty("hibernate.connection.url", url);
        hibernateProperties.setProperty("hibernate.connection.username", username);
        hibernateProperties.setProperty("hibernate.connection.password", password);
        hibernateProperties.setProperty("hibernate.connection.createDatabaseIfNotExist"
                , properties.getProperty("hibernate.connection.createDatabaseIfNotExist"));
        hibernateProperties.setProperty("hibernate.connection.useUnicode"
                , properties.getProperty("hibernate.connection.useUnicode"));
        hibernateProperties.setProperty("hibernate.connection.characterEncoding"
                , properties.getProperty("hibernate.connection.characterEncoding"));
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", properties.getProperty("hibernate.hbm2ddl.auto"));
        hibernateProperties.setProperty("hibernate.dialect", properties.getProperty("hibernate.dialect"));
        hibernateProperties.setProperty("hibernate.connection.useSSL"
                , properties.getProperty("hibernate.connection.useSSL"));
        hibernateProperties.setProperty("hibernate.connection.requireSSL"
                , properties.getProperty("hibernate.connection.requireSSL"));
        hibernateProperties.setProperty("hibernate.connection.autoReconnect"
                , properties.getProperty("hibernate.connection.autoReconnect"));
        hibernateProperties.setProperty("hibernate.connection.autoReconnectForPools"
                , properties.getProperty("hibernate.connection.autoReconnectForPools"));
        CollectionUtils.mergePropertiesIntoMap(hibernateProperties, jpaPropertyMap);

        return jpaPropertyMap;
    }
}
