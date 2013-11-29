package com.transactions.db;

import com.sigma.utils.jpa.DataManager;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Locale;
import java.util.Properties;

/**
 * User: Таранюк Михаил
 * Date: 28.07.2011 17:49:08
 */
public class DataManagerFactory {
    static EntityManagerFactory emf;
    static com.sigma.utils.jpa.DataManagerFactory dmf;
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DataBase.class);


    public static DataManager getInstance(Properties options) {
        try {
            Locale.setDefault(Locale.ENGLISH);
            Properties properties = new Properties();
            properties.setProperty("hibernate.connection.url", options.getProperty("database.url"));
            properties.setProperty("hibernate.connection.username", options.getProperty("database.user"));
            properties.setProperty("hibernate.connection.password", options.getProperty("database.password"));
            properties.setProperty("hibernate.connection.driver_class", options.getProperty("database.driver"));
            properties.setProperty("hibernate.connection.autoReconnect", "true");
            properties.setProperty("hibernate.dialect", options.getProperty("database.dialect"));
            properties.setProperty("hibernate.connection.characterEncoding", options.getProperty("database.encoding"));
            properties.setProperty("hibernate.connection.CharSet", options.getProperty("database.encoding"));
            properties.setProperty("hibernate.connection.useUnicode", "true");
//            if (emf == null)
                emf = Persistence.createEntityManagerFactory("ts-dao", properties);
//            if (dmf == null)
            dmf = new com.sigma.utils.jpa.DataManagerFactory();
            return dmf.getDataManager(emf.createEntityManager());
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException();
        }
    }
}
