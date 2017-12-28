package com.test.adCampaign.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvWriter;
import com.test.adCampaign.clickiMzAdBase.domain.ClickiMzAdBase;
import com.test.adCampaign.clickiMzAdBase.service.ClickiMzAdBaseService;
import com.test.adCampaign.infoMzCampaignSpot.service.InfoMzCampaignService;
import com.test.common.constant.ApplicationProperties;
import com.test.common.enums.BusinessDateKeyEnum;
import com.test.common.utils.*;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by lemon on 2017/7/25.
 */
@Service
public class CsvWriteService {
    private static Logger logger = LoggerFactory.getLogger(CsvWriter.class);

    @Resource
    private InfoMzCampaignService infoMzCampaignService;
    @Resource
    private ClickiMzAdBaseService clickiMzAdBaseService;
    @Resource
    private AuthService authService;

    public static final String CAMPAGIN_CSV = ApplicationProperties.getString("csv.campaign");
    public static final String SPOT_CSV = ApplicationProperties.getString("csv.spot");
    public static final String CSV_JSON_PATH = ApplicationProperties.getString("csv.json.path");
    public static final String TIMER = "T00:00:00+0800";
    public static final String CSV_STR = "T00-00-00";
    public static final String WORLD_REGION = "000000000000000000000000";
    public static final String LAND_REGION = "000000000000000000000008";
    public static final String TASK_STATUS = "ADMTASK_";

    /**
     * Created with lemon
     * Time: 2017/7/24 15:32
     * Description: 生成csv文件
     */
    public void writecsv(BusinessDateKeyEnum busDateRedisKey) {
        // 获取 token
        String access_token = authService.getToken();
        // 获取 redis中的业务日期是否符合约定 eg：2017-11-12
        String dateBefor = RedisPool.get(busDateRedisKey.getRedisKey()).toString();
        if (PubMethod.isEmpty(dateBefor) || "2016-12-31".equals(dateBefor)) {
            logger.error("业务日期有误,date={}", dateBefor);
            return;
        }
        //获取daily任务执行状态 key失效时间为1天，1表示成功不需再跑
        String taskStatus = RedisPool.get(TASK_STATUS + dateBefor) + "";
        if (BusinessDateKeyEnum.FORWARD_BUSINESS_DATE_KEY.getRedisKey().equals(busDateRedisKey.getRedisKey())) {
            if ("1".equals(taskStatus)) {
                logger.info("stm data exists already ......");
                return;
            } else {
                RedisPool.setExpire(TASK_STATUS + dateBefor, "0", 86400);
            }
        }
        //后一天日期
        String dateAfter = PubMethod.getDateStr(dateBefor, "", 1);
        //druid日期格式 eg:2017-07-23T00:00:00+0800
        String druidDateStr = dateBefor + TIMER;
        //csv写入日期格式 eg:20170723
        String dateStr = PubMethod.dateStrFormat(druidDateStr, "", "");
        // 拼接生成的csv文件名字 eg:adlemon_campaign_2017-07-23T00-00-00_2017-07-24T00-00-00.csv
        String csvNameStr = dateBefor + CSV_STR + "_" + dateAfter + CSV_STR + ".csv";
        // json文件需要的日期格式
        String jsonDate = druidDateStr + "/" + dateAfter + TIMER;

        //获取lemon数据状态，false则返回
        if ("false".equals(authService.getStatus(dateStr))) {
            logger.error("lemon data is not ready ......");
            return;
        }

        //各种格式时间表
        Map<String, String> timerMap = new HashMap<>();
        timerMap.put("dateBefor", dateBefor);
        timerMap.put("dateAfter", dateAfter);
        timerMap.put("dateStr", dateStr);
        timerMap.put("druidDateStr", druidDateStr);
        timerMap.put("jsonDate", jsonDate);

        //获取mysql当前时间下所有的campaignId、siteId 、viewId
        List<ClickiMzAdBase> allCampaignList = clickiMzAdBaseService.getCampaigns(dateBefor);
        //获取当前时间下所有的campaignId、spotId、siteId 、viewId
        List<ClickiMzAdBase> allSpotList = clickiMzAdBaseService.getSpots(dateBefor);
        //campaignId和对应的siteId viewId分组
        Map<Long, List<ClickiMzAdBase>> campaignsMap = clickiMzAdBaseService.getCampaignsMap(allCampaignList);
        //campaignId和对应的spotId siteId viewId分组
        Map<Long, Map<Long, List<ClickiMzAdBase>>> spotsMap = clickiMzAdBaseService.getSpotsMap(allSpotList);
        //info库返回的所有campaignIds,key是hashed, value是未hashed
        Map<Long, String> campaignIdMap = infoMzCampaignService.getAllCampaignId();

        // 写csv文件
        this.createCampaginCvsFile(access_token, campaignIdMap, timerMap, csvNameStr, campaignsMap);
        this.createSpotCvsFile(access_token, campaignIdMap, timerMap, csvNameStr, spotsMap);

        //切换日期
        switch (busDateRedisKey) {
            //补数成功，切换日期-1
            case BACK_BUSINESS_DATE_KEY:
                RedisPool.set(busDateRedisKey.getRedisKey(), PubMethod.getDateStr(dateBefor, "", -1));
                break;
            case FORWARD_BUSINESS_DATE_KEY:
                //上传csv到hdfs成功，切换日期+1，置为成功状态1
                RedisPool.set(busDateRedisKey.getRedisKey(), PubMethod.getDateStr(dateBefor, "", 1));
                RedisPool.setExpire(TASK_STATUS + dateBefor, "1", 86400);
                break;
            default:
                throw new RuntimeException("");
        }
        logger.info(dateBefor + " stm data has finished ......");
    }


    /**
     * Created with lemon
     * Time: 2017/7/24 15:32
     * Description: by_position=campaign
     * druidDateStr---druid的timestamp，按地域（全球的，大陆的）写pv，click
     */
    public void createCampaginCvsFile(String access_token, Map<Long, String> campaignIdMap, Map<String, String> timerMap, String csvNameStr, Map<Long, List<ClickiMzAdBase>> campaignsMap) {
        CsvWriter csvWriter = new CsvWriter(CAMPAGIN_CSV + csvNameStr, ',', Charset.forName("utf-8"));
        String[] devices = {"1007", "1001"};
        try {
            for (Long adMzCampaignIdHash : campaignsMap.keySet()) {
                //获取该campaignId相关的siteAndviews
                List<ClickiMzAdBase> siteAndviews = campaignsMap.get(adMzCampaignIdHash);
                // 获取未hash的真实campaignId
                String adMzCampaignId = campaignIdMap.get(adMzCampaignIdHash);
                String campaign_Id = String.valueOf(adMzCampaignIdHash);
                logger.error("@@@campaign -- adMzCampaignIdHash=" + adMzCampaignIdHash + "--adMzCampaignId=" + adMzCampaignId);
                //默认赋值为 0
                String pv = "0";
                String click = "0";
                String pv2 = "0";
                String click2 = "0";
                if (!PubMethod.isEmpty(adMzCampaignId)) {
                    JSONArray result = authService.getInterfaceData(access_token, timerMap.get("dateBefor"), adMzCampaignId, "campaign");
                    logger.info("result: " + result);
                    if (!PubMethod.isEmpty(result)) {
                        Iterator<Object> it = result.iterator();
                        while (it.hasNext()) {
                            JSONObject jsonObject = (JSONObject) it.next();
                            if (!PubMethod.isEmpty(jsonObject) && !PubMethod.isEmpty(jsonObject.getJSONArray("items"))) {
                                String device = jsonObject.getString("platform");
                                if ("pc".equals(device) || "mb".equals(device)) {
                                    String deviceStr = "pc".equals(device) ? "1001" : "1007";
                                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                                    Map<String, JSONObject> itemsMap = this.handleCampaigns(jsonArray);
                                    if (!PubMethod.isEmpty(itemsMap)) {
                                        JSONObject worldJson = itemsMap.get(WORLD_REGION);
                                        JSONObject landJson = itemsMap.get(LAND_REGION);
                                        if (!PubMethod.isEmpty(worldJson)) {
                                            pv = worldJson.getString("imp_day");
                                            click = worldJson.getString("clk_day");
                                        }
                                        if (!PubMethod.isEmpty(landJson)) {
                                            pv2 = landJson.getString("imp_day");
                                            click2 = landJson.getString("clk_day");
                                        }
                                        this.writeCampaignFile(csvWriter, timerMap, siteAndviews, campaign_Id, deviceStr, pv, click, pv2, click2);
                                    }
                                }
                            }
                        }
                    } else { //请求返回为空的情况下直接写0（包括400）
                        for (String deviceStr : devices) {
                            this.writeCampaignFile(csvWriter, timerMap, siteAndviews, campaign_Id, deviceStr, pv, click, pv2, click2);
                        }
                    }
                } else { //info库中不存在的campaignId直接写文件, pv && click为0
                    for (String deviceStr : devices) {
                        this.writeCampaignFile(csvWriter, timerMap, siteAndviews, campaign_Id, deviceStr, pv, click, pv2, click2);
                    }
                    logger.error(adMzCampaignIdHash + " 不存在于info库");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            csvWriter.close();
        }
        //生成json文件
        VelocityUtil.createJson(timerMap.get("jsonDate"), "adlemon_campaign_base_" + csvNameStr, "csv_campaign.vm", CSV_JSON_PATH, "csv_campaign.json");
        //上传csv到hdfs
        HdfsUtil.uploadFile(CAMPAGIN_CSV + csvNameStr);
    }

    /**
     * Created with lemon
     * Time: 2017/7/24 15:34
     * Description: by_position=spot
     */

    public void createSpotCvsFile(String access_token, Map<Long, String> campaignIdMap, Map<String, String> timerMap, String
            csvNameStr, Map<Long, Map<Long, List<ClickiMzAdBase>>> spotsMap) {
        CsvWriter csvWriter = new CsvWriter(SPOT_CSV + csvNameStr, ',', Charset.forName("utf-8"));
        String[] devices = {"1007", "1001"};
        try {
            for (Long adMzCampaignIdHash : spotsMap.keySet()) {
                // 获取未hash的真实campaignId
                String adMzCampaignId = campaignIdMap.get(adMzCampaignIdHash);
                String campaign_Id = String.valueOf(adMzCampaignIdHash);
                logger.error("@@@spot -- adMzCampaignIdHash=" + adMzCampaignIdHash + "--adMzCampaignId=" + adMzCampaignId);
                String spot_Id = null;
                if (!PubMethod.isEmpty(adMzCampaignId)) {
                    JSONArray result = authService.getInterfaceData(access_token, timerMap.get("dateBefor"), adMzCampaignId, "spot");
                    if (!PubMethod.isEmpty(result)) {
                        Iterator<Object> it = result.iterator();
                        while (it.hasNext()) {
                            JSONObject jsonObject = (JSONObject) it.next();
                            if (!PubMethod.isEmpty(jsonObject)) {
                                String device = jsonObject.getString("platform");
                                if ("pc".equals(device) || "mb".equals(device)) {
                                    String deviceStr = "pc".equals(device) ? "1001" : "1007";
                                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                                    Map<Long, List<ClickiMzAdBase>> spotListByCampaign = spotsMap.get(adMzCampaignIdHash);
                                    Map<Long, Map<String, JSONObject>> itemsMap = this.handleSpots(jsonArray, spotListByCampaign);
                                    if (!PubMethod.isEmpty(itemsMap)) {
                                        for (Long spotIdHash : itemsMap.keySet()) {
                                            spot_Id = String.valueOf(spotIdHash);
                                            // 默认赋值为 0
                                            String pv = "0";
                                            String click = "0";
                                            String pv2 = "0";
                                            String click2 = "0";
                                            if (!PubMethod.isEmpty(itemsMap.get(spotIdHash))) {
                                                JSONObject worldJson = itemsMap.get(spotIdHash).get(WORLD_REGION);
                                                JSONObject landJson = itemsMap.get(spotIdHash).get(LAND_REGION);
                                                if (!PubMethod.isEmpty(worldJson)) {
                                                    pv = worldJson.getString("imp_day");
                                                    click = worldJson.getString("clk_day");
                                                }
                                                if (!PubMethod.isEmpty(landJson)) {
                                                    pv2 = landJson.getString("imp_day");
                                                    click2 = landJson.getString("clk_day");
                                                }
                                            }
                                            List<ClickiMzAdBase> siteAndviews = spotListByCampaign.get(spotIdHash);
                                            this.writeSpotFile(csvWriter, timerMap, siteAndviews, campaign_Id, spot_Id, deviceStr, pv, click, pv2, click2);
                                        }
                                    }
                                }
                            }
                        }
                    } else { //返回为空情况写0
                        for (String deviceStr : devices) {
                            this.writeSpotFileUtil(csvWriter, timerMap, spotsMap, adMzCampaignIdHash, deviceStr);
                        }
                    }
                } else { //info库中找不到campaignId直接写0
                    for (String deviceStr : devices) {
                        this.writeSpotFileUtil(csvWriter, timerMap, spotsMap, adMzCampaignIdHash, deviceStr);
                    }
                    logger.error(adMzCampaignIdHash + " 不存在于info库");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            csvWriter.close();
        }
        //生成json文件
        VelocityUtil.createJson(timerMap.get("jsonDate"), "adlemon_spot_base_" + csvNameStr, "csv_spot.vm", CSV_JSON_PATH, "csv_spot.json");
        //上传csv到hdfs,如果成功状态改为1,下次任务补跑，如果不成功,状态还是0
        HdfsUtil.uploadFile(SPOT_CSV + csvNameStr);
    }

    /**
     * Created with lemon
     * Time: 2017/9/4 11:27
     * Description: 把jsonArray封装成map,获取全球的和大陆的数据,以item为单位
     */
    public Map<String, JSONObject> handleCampaigns(JSONArray jsonArray) {
        Map<String, JSONObject> map = new HashedMap();
        Iterator<Object> it = jsonArray.iterator();
        while (it.hasNext()) {
            JSONObject jsonObject = (JSONObject) it.next();
            if (!PubMethod.isEmpty(jsonObject)) {
                String regionId = jsonObject.getJSONObject("attributes").getString("region_id");
                map.put(regionId, jsonObject.getJSONObject("metrics"));
            }
        }
        return map;
    }

    /**
     * Created with lemon
     * Time: 2017/9/5 10:55
     * Description: 以spot_id_str为单位分组
     */
    public Map<Long, Map<String, JSONObject>> handleSpots(JSONArray jsonArray, Map<Long, List<ClickiMzAdBase>> spotListByCampaign) {
        //1、解析接口 返回数据
        Map<Long, Map<String, JSONObject>> map = new HashedMap();
        Iterator<Object> it = jsonArray.iterator();
        while (it.hasNext()) {
            //遍历item
            JSONObject jsonObject = (JSONObject) it.next();
            if (!PubMethod.isEmpty(jsonObject)) {
                String spotId = jsonObject.getJSONObject("attributes").getString("spot_id_str");
                String regionId = jsonObject.getJSONObject("attributes").getString("region_id");
                long spotIdHash = MZEncryptUtil.hash(spotId);
                //按照 spotId 分组
                if (PubMethod.isEmpty(map.get(spotIdHash))) {
                    Map<String, JSONObject> spotIdMap = new HashedMap();
                    spotIdMap.put(regionId, jsonObject.getJSONObject("metrics"));
                    map.put(spotIdHash, spotIdMap);
                } else {
                    Map<String, JSONObject> spotIdMap = map.get(spotIdHash);
                    spotIdMap.put(regionId, jsonObject.getJSONObject("metrics"));
                    map.put(spotIdHash, spotIdMap);
                }
            }
        }
        //2、按照stat库的soptId, 拼装接口的spotid,接口没有的，放null
        Map<Long, Map<String, JSONObject>> result = new HashedMap();
        for (Long spotId : spotListByCampaign.keySet()) {
            if (PubMethod.isEmpty(map.get(spotId))) {
                result.put(spotId, null);
            } else {
                result.put(spotId, map.get(spotId));
            }
        }
        logger.error("spotListByCampaign.size()=" + spotListByCampaign.size() + "--result.size=" + result.size());
        return result;
    }


    /**
     * Created with lemon
     * Time: 2017/9/11 16:42
     * Description:  写csv文件
     */

    public boolean writeSpotFile(CsvWriter csvWriter, Map<String, String> timerMap, List<ClickiMzAdBase> siteAndviews, String
            campaignIdHash, String spotIdHash, String device, String pv, String click, String pv2, String click2) throws IOException {
        if (!PubMethod.isEmpty(siteAndviews)) {
            for (ClickiMzAdBase clickiMzAdBase : siteAndviews) {
                String siteId = String.valueOf(clickiMzAdBase.getSiteId());
                String viewId = String.valueOf(clickiMzAdBase.getViewId());
                String[] content = {timerMap.get("druidDateStr"), timerMap.get("dateStr"), campaignIdHash, spotIdHash, siteId, viewId, device, pv, click, pv2, click2};
                csvWriter.writeRecord(content);
                logger.info(campaignIdHash + "--" + spotIdHash + " " + siteId + " " + viewId + " " + device + " " + pv + " " + click + " " + pv2 + " " + click2);
            }
        }
        return true;
    }

    /**
     * Created with lemon
     * Time: 2017/9/11 17:42
     * Description:  写csv文件
     */
    public boolean writeCampaignFile(CsvWriter csvWriter, Map<String, String> timerMap, List<ClickiMzAdBase> siteAndviews, String
            campaignIdHash, String device, String pv, String click, String pv2, String click2) throws IOException {
        if (!PubMethod.isEmpty(siteAndviews)) {
            for (ClickiMzAdBase clickiMzAdBase : siteAndviews) {
                String siteId = String.valueOf(clickiMzAdBase.getSiteId());
                String viewId = String.valueOf(clickiMzAdBase.getViewId());
                String[] content = {timerMap.get("druidDateStr"), timerMap.get("dateStr"), campaignIdHash, siteId, viewId, device, pv, click, pv2, click2};
                csvWriter.writeRecord(content);
                logger.info(campaignIdHash + "**" + " " + siteId + " " + viewId + " " + device + " " + pv + " " + click + " " + pv2 + " " + click2);
            }
        }
        return true;
    }

    /**
     * Created with lemon
     * Time: 2017/9/12 12:19
     * Description: pv uv为0情况 各种异常处理
     */

    public void writeSpotFileUtil(CsvWriter csvWriter, Map<String, String> timerMap, Map<Long, Map<Long, List<ClickiMzAdBase>>> spotsMap,
                                  long adMzCampaignIdHash, String device) throws IOException {
        Map<Long, List<ClickiMzAdBase>> spots = spotsMap.get(adMzCampaignIdHash);
        if (!PubMethod.isEmpty(spots)) {
            for (Long adMzspotIdHash : spots.keySet()) {
                if (!PubMethod.isEmpty(adMzspotIdHash)) {
                    String spot_Id = String.valueOf(adMzspotIdHash);
                    List<ClickiMzAdBase> siteAndviews = spots.get(adMzspotIdHash);
                    if (!PubMethod.isEmpty(siteAndviews)) {
                        this.writeSpotFile(csvWriter, timerMap, siteAndviews, String.valueOf(adMzCampaignIdHash), spot_Id, device, "0", "0", "0", "0");
                    }
                }
            }
        } else {
            logger.error(adMzCampaignIdHash + "没有广告位");
        }
    }
}




