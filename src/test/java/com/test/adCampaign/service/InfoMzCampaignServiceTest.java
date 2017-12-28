package com.test.adCampaign.service;

import com.alibaba.fastjson.JSONObject;
import com.test.Application;
import com.test.adCampaign.clickiMzAdBase.service.ClickiMzAdBaseService;
import com.test.adCampaign.infoMzCampaignSpot.service.InfoMzCampaignService;
import com.test.common.enums.BusinessDateKeyEnum;
import com.test.common.utils.HttpClientUtil;
import com.test.common.utils.PubMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lemon on 2017/7/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class InfoMzCampaignServiceTest {
    @Resource
    private ClickiMzAdBaseService clickiMzAdBaseService;
    @Resource
    private CsvWriteService csvWriteService;
    @Resource
    private InfoMzCampaignService infoMzCampaignService;
    @Resource
    private AuthService authService;

    @Test
    public void testTask() {

        //clickiMzAdBaseService.querry("2017-09-04");
        infoMzCampaignService.getAllCampaignId();
    }

    @Test
    public void csvWrite_reload() {

        String AUTH_URL = "http://api.cn.miaozhen.com/adlemon/v1/reports/basic/show";
        Map<String, String> accessMap = new HashMap<>();
        String access_token = authService.getToken();
        accessMap.put("access_token", access_token);
        accessMap.put("date", "2017-09-06");
        accessMap.put("metrics", "day");
        accessMap.put("campaign_id", "2054059");
        accessMap.put("by_position", "campaign");
        JSONObject result = HttpClientUtil.httpGet(AUTH_URL, accessMap);
        String pv = "0";
        String click = "0";
        String pv2 = "0";
        String click2 = "0";
        if (!PubMethod.isEmpty(result)) {
            System.out.println(result.getString("error_description"));
            //System.out.println(pv + "  " + click + "  " + pv2 + click2);
        }
        if ("the date is out of campaign period.".equals(result.getString("error_description"))) {
            System.out.println("7777777");
        }
    }

    @Test
    public void csvWrite() {
        this.csvWriteService.writecsv(BusinessDateKeyEnum.BACK_BUSINESS_DATE_KEY);
    }

}
