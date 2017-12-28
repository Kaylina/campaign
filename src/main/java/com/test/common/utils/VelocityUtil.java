package com.test.common.utils;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.collections.map.HashedMap;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.Map;

/**
 * @Author: lemon
 * @Time: 2017/7/27 11:16
 * @Describe:
 */
public class VelocityUtil {

    private static Logger logger = LoggerFactory.getLogger(VelocityUtil.class);
    private static VelocityEngine velocityEngine = null;
    private static final String TEMPLATE_LOCAL_DIRECTORY = "templates/";


    static {
        logger.debug("velocityEngine init...");
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();
        logger.debug("velocityEngine successed...");
    }

    /**
     * @Author: lemon
     * @Time: 2017/7/27 11:28
     * @Describe: 生成JSON 文件
     */
    public static String mergeTemplateFile(String templateFileName, Map<String, Object> map, String newFilePath, String newFileName) {
        String body = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, TEMPLATE_LOCAL_DIRECTORY + templateFileName, "UTF-8", map);
        String newFileFullPath = FileUtil.createJsonFile(body, newFilePath, newFileName);
        return newFileFullPath;
    }

    public static void createJson(String dateStr, String csvPath, String fileName, String jsonPath, String jsonName) {
        Map<String, Object> map = new HashedMap();
        JSONArray js = new JSONArray();
        js.add(dateStr);
        map.put("date", js.toString());
        map.put("filePath", "/druid/csv/" + csvPath);
        VelocityUtil.mergeTemplateFile(fileName, map, jsonPath, jsonName);
    }

    public static void createDruidQueryJson(String dateStr, String fileName, String jsonPath, String jsonName) {
        Map<String, Object> map = new HashedMap();
        JSONArray js = new JSONArray();
        js.add(dateStr);
        map.put("date", js.toString());
        VelocityUtil.mergeTemplateFile(fileName, map, jsonPath, jsonName);
    }
}
