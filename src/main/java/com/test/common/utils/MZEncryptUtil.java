package com.test.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Iterator;

public class MZEncryptUtil {
    public static String md5(String str) {
        if (str == null) {
            return null;
        }
        try {
            return DigestUtils.md5Hex(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static long hash(String str) {
        if (str == null || str.trim().equals("")) {
            return 0;
        }
        String ss = md5(str).substring(1, 15);
        return Long.parseLong(ss, 16);
    }

    public static void main(String[] args) {
       /* File file = new File("E:/20170929_hj.txt");
        BufferedReader lineReader = null;
        BufferedWriter writer = null;
        StringBuffer buffer = null;
        String line = null;
        try {
            lineReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            writer = new BufferedWriter(new FileWriter("E:/campaignId.txt", true));
            while ((line = lineReader.readLine()) != null) {
                writer.write(MZEncryptUtil.hash(line) + "");
                writer.newLine();
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        String a = "[{\"platform\":\"pc\",\"items\":[{\"metrics\":{\"uim_day\":0,\"clk_day\":472,\"ucl_day\":407,\"imp_day\":0},\"attributes\":{\"spot_id\":\"104729428\",\"publisher_id\":\"1\",\"audience\":\"all\",\"region_id\":\"000000000000000000000000\",\"universe\":\"-\",\"spot_id_str\":\"75QuK\"}},{\"metrics\":{\"uim_day\":0,\"clk_day\":3667,\"ucl_day\":3077,\"imp_day\":0}}],\"date\":\"2017-04-01\",\"campaign_id\":\"2038678\",\"version\":0},{\"platform\":\"mb\",\"items\":[{\"metrics\":{\"uim_day\":0,\"clk_day\":3667,\"ucl_day\":3077,\"imp_day\":0}}],\"date\":\"2017-04-01\",\"campaign_id\":\"2038678\",\"version\":0}]";
        JSONArray result = JSONArray.parseArray(a);
        Iterator<Object> it = result.iterator();
        while (it.hasNext()) {
            JSONObject jsonObject = (JSONObject) it.next();
            String device = jsonObject.getString("platform");
            // System.out.println(device);
        }

        System.out.println(MZEncryptUtil.hash("2042099"));
    }
}
