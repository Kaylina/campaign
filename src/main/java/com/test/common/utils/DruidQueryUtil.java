package com.test.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

public class DruidQueryUtil {
    private static final String DRUID_URL = "http://d160.mzhen.cn:8180/druid/v2";
    private static CloseableHttpClient httpclient = HttpClients.createDefault();
    private static HttpPost httppost = new HttpPost(DRUID_URL);

    static {
        httppost.setHeader("Content-Type", "application/json");
    }

    public static String query(String json) {
        String content = "";
        try {
            httppost.setEntity(new StringEntity(json));
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    content = EntityUtils.toString(entity, "UTF-8");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        System.out.println(content);
        return content;
    }

    public static <T> List<T> queryParse(String json, Class<T> cls) {
        List<T> list = new LinkedList<T>();
        String response = query(json);
        JSONArray items = JSONArray.parseArray(response);
        T model = null;
        int size = items.size();
        System.out.println(items.size());
        for (int i = 0; i < size; i++) {
            JSONObject item = items.getJSONObject(i);
            model = item.getObject("event", cls);
            System.out.println(model);
            list.add(model);
        }
        return list;
    }
}
