package com.zjlp.face.titan.migration;

import com.zjlp.face.titan.common.utils.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class MigrationConfig {
    private static final Logger logger = LoggerFactory.getLogger(MigrationConfig.class);
    private static volatile Properties properties;

    public static Properties getProperties() {
        if (properties == null) {
            synchronized (MigrationConfig.class) {
                if (properties == null) {
                    String path = System.getProperty("data-migration.properties");
                    if (path == null || path.length() == 0) {
                        path = "data-migration.properties";
                    }
                    properties = Config.loadProperties(path, false, true);
                }
            }
        }
        return properties;
    }

    public static String get(String key) {
        String value = getProperties().getProperty(key);
        try {
            if (value == null)
                logger.error("配置文件中沒有这个属性:" + key);
            value = new String(value.getBytes("ISO-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }

}
