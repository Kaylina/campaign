package com.test.adCampaign.infoMzCampaignSpot.service;


import com.test.adCampaign.infoMzCampaignSpot.dao.InfoMzCampaignSpotMapper;
import com.test.adCampaign.infoMzCampaignSpot.domain.InfoMzCampaignSpot;
import com.test.common.utils.MZEncryptUtil;
import com.test.common.utils.PubMethod;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by lemon on 2017/7/13.
 */
@Service
public class InfoMzCampaignService {
    private static Logger logger = LoggerFactory.getLogger(InfoMzCampaignService.class);

    @Resource
    private InfoMzCampaignSpotMapper infoMzCampaignSpotMapper;

    // 获取所有campaignId，和其hash值，放进map
    public Map<Long, String> getAllCampaignId() {

        List<InfoMzCampaignSpot> list = infoMzCampaignSpotMapper.selectAll();
        Map<Long, String> campaignIdMap = new HashedMap();
        for (InfoMzCampaignSpot infoMzCampaignSpot : list) {
            if (!PubMethod.isEmpty(infoMzCampaignSpot)) {
                campaignIdMap.put(MZEncryptUtil.hash(infoMzCampaignSpot.getCampaignId()), infoMzCampaignSpot.getCampaignId());
            }
        }
        return campaignIdMap;

    }

}
