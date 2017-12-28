package com.test.adCampaign.tasks;

import com.test.adCampaign.service.CsvWriteService;
import com.test.common.enums.BusinessDateKeyEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CampaignTask {

    private static Logger logger = LoggerFactory.getLogger(CampaignTask.class);

    @Resource
    private CsvWriteService csvWriteService;


    /**
     * Created with lemon
     * Time: 2017/7/16 22:04
     * Description: 定时任务
     * 第一位，表示秒，取值0-59 *
     * 第二位，表示分，取值0-59 *
     * 第三位，表示小时，取值0-23 *
     * 第四位，日期天/日，取值1-31 *
     * 第五位，日期月份，取值1-12 *
     * 第六位，星期，取值1-7，星期一，星期二...，注：不是第1周，第二周的意思 另外：1表示星期天，2表示星期一*
     * 第7为，年份，可以留空，取值1970-2099
     */


    //定时任务，规定一个可能的最早和最晚时间，每天6-10点跑，拿昨天的数据，正常跑，日期+1
    @Scheduled(cron = "0 15 * * * ?")
    public void csvWrite() {
        logger.error("daily tasks start to run .....");
        this.csvWriteService.writecsv(BusinessDateKeyEnum.FORWARD_BUSINESS_DATE_KEY);

    }

   /* //定时任务，每天12-23点之间跑，补数，倒着跑，日期-1
    @Scheduled(cron = "0 15 12-23/1,0-7/1 * * ?")
    public void csvWrite_reload() {
        logger.debug("reload---生成csv文件.....");
        this.csvWriteService.writecsv(BusinessDateKeyEnum.BACK_BUSINESS_DATE_KEY);

    }*/

}


