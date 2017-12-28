package com.test.adCampaign.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.test.common.constant.ApplicationProperties;
import com.test.common.utils.HttpClientUtil;
import com.test.common.utils.PubMethod;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lemon on 2017/7/16.
 */
@Component
public class AuthService {

    private static Logger logger = LoggerFactory.getLogger(AuthService.class);
    public static final String MZ_USERNAME = ApplicationProperties.getString("auth.username");
    public static String MZ_PASSWORD = ApplicationProperties.getString("auth.password");
    public static String MZ_grant_type = ApplicationProperties.getString("auth.grant_type");
    public static String MZ_HOST = ApplicationProperties.getString("auth.host");
    public static String MZ_CLIENT_ID = ApplicationProperties.getString("auth.client_id");
    public static String MZ_CLIENT_SECRET = ApplicationProperties.getString("auth.client_secret");
    public static String AUTH_URL = ApplicationProperties.getString("auth.url");
    public static String lemon_URL = ApplicationProperties.getString("lemon.url");

    /**
     * Created with lemon
     * Time: 2017/7/16 21:52
     * Description: 获取token
     */
    //@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public String getToken() {

        Map<String, String> map = new HashMap<String, String>();
        map.put("grant_type", MZ_grant_type);
        map.put("username", MZ_USERNAME);
        map.put("password", MZ_PASSWORD);
        map.put("client_id", MZ_CLIENT_ID);
        map.put("client_secret", MZ_CLIENT_SECRET);
        JSONObject result = HttpClientUtil.httpPost(MZ_HOST, map);
        String refresh_token = result.getString("refresh_token");
        String access_token = result.getString("access_token");
        return access_token;
    }

    /**
     * @Author: lemon
     * @Time: 2017/9/21 16:47
     * @Describe: 调用接口获取数据
     */
    public JSONArray getInterfaceData(String accessToken, String dateBefor, String adMzCampaignId, String byPosition) throws Exception {
        Map<String, String> accessMap = new HashMap<>();
        accessMap.put("access_token", accessToken);
        accessMap.put("date", dateBefor);
        accessMap.put("metrics", "day");
        accessMap.put("platform", "all");
        accessMap.put("campaign_id", adMzCampaignId);
        accessMap.put("by_position", byPosition);
        JSONArray result = HttpClientUtil.httpGetJsonArray(AUTH_URL, accessMap);
        return result;
    }

    /**
     * Created with lemon
     * Time: 2017/11/1 14:29
     * Description: 检查lemon数据状态
     */

    public String getStatus(String dateStr) {
        Map<String, String> paramMap = new HashedMap();
        paramMap.put("date", dateStr);
        String status = null;
        JSONObject result = HttpClientUtil.httpGetStr(lemon_URL, paramMap);
        if (!PubMethod.isEmpty(result)) {
            status = result.getString("data");
            logger.error("lemon data status={}", status);
        }
        return status;
    }
}
