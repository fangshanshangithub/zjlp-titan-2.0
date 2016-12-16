package com.zjlp.face.titan.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class ConfigUtil {
    private static final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);

    private static volatile Properties properties;

    public static Properties getProperties() {
        if (properties == null) {
            synchronized (ConfigUtil.class) {
                if (properties == null) {
                    String path = System.getProperty("config.properties");
                    logger.info("外部的配置文件:" + path);
                    if (path == null || path.length() == 0) {
                        path = ConfigUtil.class.getClassLoader().getResource("config.properties").getPath();
                    }

                    try {
                        Properties prop = new Properties();
                        FileInputStream input = new FileInputStream(path);
                        try {
                            prop.load(input);
                        } finally {
                            input.close();
                        }
                        properties = prop;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
