package com.test.adCampaign.clickiMzAdBase.service;

import com.test.adCampaign.clickiMzAdBase.dao.ClickiMzAdBaseMapper;
import com.test.adCampaign.clickiMzAdBase.domain.ClickiMzAdBase;
import com.test.common.utils.PubMethod;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by lemon on 2017/8/31.
 */
@Service
public class ClickiMzAdBaseService {
    private static Logger logger = LoggerFactory.getLogger(ClickiMzAdBaseService.class);

    @Resource
    private ClickiMzAdBaseMapper clickiMzAdBaseMapper;

    /**
     * @Author: lemon
     * @Time: 2017/9/1 16:16
     * @Describe: 查询结果分组，Map<admCampaignId, List<siteAndviews> >
     */
    public Map<Long, List<ClickiMzAdBase>> getCampaignsMap(List<ClickiMzAdBase> clickiMzAdBaseList) {
        Map<Long, List<ClickiMzAdBase>> campaignsMap = new HashedMap();
        for (ClickiMzAdBase clickiMzAdBase : clickiMzAdBaseList) {
            if (PubMethod.isEmpty(campaignsMap.get(clickiMzAdBase.getAdMzCampaignId()))) {
                List<ClickiMzAdBase> list = new ArrayList<>();
                list.add(clickiMzAdBase);
                campaignsMap.put(clickiMzAdBase.getAdMzCampaignId(), list);
            } else {
                List<ClickiMzAdBase> list = campaignsMap.get(clickiMzAdBase.getAdMzCampaignId());
                list.add(clickiMzAdBase);
                campaignsMap.put(clickiMzAdBase.getAdMzCampaignId(), list);
            }
        }
        logger.error("campaignsMap.size()={}", campaignsMap.size());
        return campaignsMap;
    }

    /**
     * Created with lemon
     * Time: 2017/9/11 14:18
     * Description:查询结果分组：Map<admCampaignId, Map<spotId, List<siteAndviews> > >
     */
    public Map<Long, Map<Long, List<ClickiMzAdBase>>> getSpotsMap(List<ClickiMzAdBase> clickiMzAdBases) {
        Map<Long, Map<Long, List<ClickiMzAdBase>>> spotsMap = new HashedMap();
        for (ClickiMzAdBase clickiMzAdBase : clickiMzAdBases) {
            long admCampaignId = clickiMzAdBase.getAdMzCampaignId();
            long adMzSpotId = clickiMzAdBase.getAdMzSpotId();
            if (PubMethod.isEmpty(spotsMap.get(admCampaignId))) {
                Map<Long, List<ClickiMzAdBase>> map = new HashedMap();
                if (PubMethod.isEmpty(map.get(adMzSpotId))) {
                    List<ClickiMzAdBase> list = new ArrayList<>();
                    list.add(clickiMzAdBase);
                    map.put(adMzSpotId, list);
                } else {
                    List<ClickiMzAdBase> list = spotsMap.get(admCampaignId).get(adMzSpotId);
                    list.add(clickiMzAdBase);
                    map.put(adMzSpotId, list);
                    logger.info(admCampaignId + "--" + adMzSpotId);
                }
                spotsMap.put(admCampaignId, map);
            } else {
                Map<Long, List<ClickiMzAdBase>> map = spotsMap.get(admCampaignId);
                if (PubMethod.isEmpty(map.get(adMzSpotId))) {
                    List<ClickiMzAdBase> list = new ArrayList<>();
                    list.add(clickiMzAdBase);
                    map.put(adMzSpotId, list);
                } else {
                    List<ClickiMzAdBase> list = spotsMap.get(admCampaignId).get(adMzSpotId);
                    list.add(clickiMzAdBase);
                    map.put(adMzSpotId, list);
                }
                spotsMap.put(admCampaignId, map);
            }
        }
        logger.error("getSpotsMap.size()={}", spotsMap.size());
        return spotsMap;
    }


    /**
     * Created with lemon
     * Time: 2017/9/11 11:32
     * Description: 获取当前时间下所有的campaignId、siteId 、viewId
     */
    public List<ClickiMzAdBase> getCampaigns(String dateStr) {
        List<ClickiMzAdBase> clickiMzAdBaseList = clickiMzAdBaseMapper.selectByDate(Long.parseLong(PubMethod.getDateStr(dateStr, "yyyyMMdd", 0)));
        logger.error("Campaignlist.size()={}", clickiMzAdBaseList.size());
        return clickiMzAdBaseList;
    }

    /**
     * Created with lemon
     * Time: 2017/9/11 11:33
     * Description: 获取当前时间下所有活动campaignId、spot、siteId 、viewId
     */
    public List<ClickiMzAdBase> getSpots(String dateStr) {
        List<ClickiMzAdBase> spotList = clickiMzAdBaseMapper.selectSpots(Long.parseLong(PubMethod.getDateStr(dateStr, "yyyyMMdd", 0)));
        logger.error("Spotslist.size()={}", spotList.size());
        return spotList;
    }

    /**
     * Created with lemon
     * Time: 2017/9/11 11:34
     * Description: 获取所有的spotId
     */
    public HashSet<Long> getAllSpotId(List<ClickiMzAdBase> spotList) {
        HashSet<Long> spotIdSet = new HashSet<>();
        for (ClickiMzAdBase spot : spotList) {
            if (!PubMethod.isEmpty(spot)) {
                spotIdSet.add(spot.getAdMzSpotId());
            }
        }
        logger.error("AllSpotId.size={}", spotIdSet.size());
        return spotIdSet;
    }

}
