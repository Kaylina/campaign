package com.test.common.utils;


import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class BundleUtil {

    public static final Logger LOG = Logger.getLogger(BundleUtil.class);
    private static final Map<String, BundleUtil> bundleMap = new HashMap<String, BundleUtil>();
    private ResourceBundle bundle;

    public static BundleUtil newInstance(String name) {
        if (null == name || "".equals(name)) {
            return new BundleUtil(null);
        }
        if (!bundleMap.containsKey(name)) {
            synchronized (BundleUtil.class) {
                if (!bundleMap.containsKey(name)) {
                    bundleMap.put(name, new BundleUtil(name));
                }
            }
        }
        return bundleMap.get(name);
    }

    public String getString(String key) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            LOG.warn(String.format("-----Can't find %s' value-----", key));
            return "";
        }
    }

    public String getString(String key, String defaultValue) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            LOG.warn(String.format("-----Can't find %s' value-----", key));
        }
        return defaultValue;
    }

    private BundleUtil(String name) {
        try {
            bundle = ResourceBundle.getBundle(name);
        } catch (Exception e) {
            LOG.warn(String.format("Can't find file: %s.property", name));
        }
    }
}
