package com.test.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with lemon
 * Time: 2017/7/16 22:19
 * Description: HttpClient 工具类
 */
public class HttpClientUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
    private static final String CHARSET = "utf-8";

    /**
     * Created with lemon
     * Time: 2017/7/16 22:18
     * Description: httpPost
     */
    public static JSONObject httpPost(String url, Map<String, String> paramMap) {
        String jsonStr = httpPost(url, paramMap, CHARSET);
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        return jsonObject;
    }

    /**
     * Created with lemon
     * Time: 2017/7/16 22:18
     * Description: httpGet返回jsonObject
     */
    public static JSONObject httpGet(String url, Map<String, String> paramMap) {
        String paramStr = createLinkString(paramMap);
        String jsonStr = httpGet(url + "?" + paramStr);
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        return jsonObject;
    }

    /**
     * Created with lemon
     * Time: 2017/9/2710:21
     * Description: 返回jsonArray
     */
    public static JSONArray httpGetJsonArray(String url, Map<String, String> paramMap) throws Exception {
        String paramStr = createLinkString(paramMap);
        String jsonStr = httpGet(url + "?" + paramStr);
        if ("400".equals(jsonStr)) {
            jsonStr = "";
        }
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        return jsonArray;
    }

    /**
     * Created with lemon
     * Time: 2017/11/1 14:19
     * Description: 判断lemon状态信息
     */
    public static JSONObject httpGetStr(String url, Map<String, String> paramMap) {
        String jsonStr = httpGet(url + paramMap.get("date"));
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        return jsonObject;
    }

    /**
     * @param url
     * @param paramMap
     * @param charset
     * @return
     */
    public static String httpPost(String url, Map<String, String> paramMap, String charset) {
        logger.info("httpPost url: " + url);
        logger.info("httpPost param: " + JSONObject.toJSONString(paramMap));
        // HttpClient
        CloseableHttpClient client = httpClientBuilder.build();
        HttpPost method = new HttpPost(url);
        // 设置请求和传输超时时
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
        method.setConfig(requestConfig);
        try {
            if (null != paramMap) {
                List<NameValuePair> list = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> e : paramMap.entrySet()) {
                    list.add(new BasicNameValuePair(e.getKey(), e.getValue()));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                method.setEntity(entity);
            }
            CloseableHttpResponse response = client.execute(method);
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            logger.info("远程访问结果 ： " + result);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return result;
            } else {
                logger.error("httpPost请求失败：" + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Created with: lemon.
     * Date: 2016/9/20  11:45
     * Description: httpGet
     */
    public static String httpGet(String url) {

        // HttpClient
        CloseableHttpClient client = httpClientBuilder.build();
        // 发get请求
        HttpGet request = new HttpGet(url);
        // 设置请求和传输超时
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
        //默认失败后重发3次
        //HttpClientBuilder.create().setRetryHandler(new DefaultHttpRequestRetryHandler(3, true));
        //HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setRetryHandler(new DefaultHttpRequestRetryHandler()).build();  //默认失败后重发3次，可用别的构造方法指定重发次数
        request.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        int i = 0;
        while (i < 3) {
            try {
                logger.info("httpGet url: " + url);
                response = client.execute(request);
                logger.error("httpGet response：" + response);
            } catch (Exception e) {
                if (i > 2) {
                    break;
                }
                i++;
            }

            if (response != null) {
                break;
            }
        }
        try {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                logger.info("httpGet请求：" + response.getStatusLine().getStatusCode());
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
                logger.info("httpGet请求：" + response.getStatusLine().getStatusCode());
                return "400";
            } else {
                logger.error("httpGet请求失败：" + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Created with lemon
     * Time: 2017/7/16 22:17
     * Description: GET 参数拼装
     */

    public static String createLinkString(Map<String, String> params) {
        logger.info("httpGet param: " + JSONObject.toJSONString(params));
        List<String> keys = new ArrayList<String>(params.keySet());
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

}
