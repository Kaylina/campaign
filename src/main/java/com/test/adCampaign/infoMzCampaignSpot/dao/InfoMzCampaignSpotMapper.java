package com.test.adCampaign.infoMzCampaignSpot.dao;

import com.test.adCampaign.infoMzCampaignSpot.domain.InfoMzCampaignSpot;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InfoMzCampaignSpotMapper {
    int deleteByPrimaryKey(@Param("cId") Long cId, @Param("sId") Long sId);

    int insert(InfoMzCampaignSpot record);

    InfoMzCampaignSpot selectByPrimaryKey(@Param("cId") Long cId, @Param("sId") Long sId);

    List<InfoMzCampaignSpot> selectAll();

    int updateByPrimaryKey(InfoMzCampaignSpot record);
}