package com.test.adCampaign.service;

import com.alibaba.fastjson.JSONObject;
import com.test.common.utils.HttpClientUtil;
import com.test.common.utils.PubMethod;
import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * Created by lemon on 2017/10/31.
 */
public class Test {


    public boolean getStatus(String dateStr) {
        Map<String, String> paramMap = new HashedMap();
        paramMap.put("date", dateStr);
        JSONObject result = HttpClientUtil.httpGetStr("http://2308.mzhen.cn:8090/calcprogress/", paramMap);
        if (!PubMethod.isEmpty(result)) {
            String status = result.getString("data");
            if ("true".equals(status)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String args[]) {
        
        System.out.println();
    }
}
