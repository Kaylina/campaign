package com.test.adCampaign.clickiMzAdBase.dao;

import com.test.adCampaign.clickiMzAdBase.domain.ClickiMzAdBase;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClickiMzAdBaseMapper {
    int deleteByPrimaryKey(@Param("date") Long date, @Param("siteId") Long siteId, @Param("viewId") Long viewId, @Param("adMzCampaignId") Long adMzCampaignId, @Param("adMzSpotId") Long adMzSpotId, @Param("adMzKeywordId") Long adMzKeywordId, @Param("adMzCookieId") Long adMzCookieId, @Param("hour") Long hour);

    int insert(ClickiMzAdBase record);

    ClickiMzAdBase selectByPrimaryKey(@Param("date") Long date, @Param("siteId") Long siteId, @Param("viewId") Long viewId, @Param("adMzCampaignId") Long adMzCampaignId, @Param("adMzSpotId") Long adMzSpotId, @Param("adMzKeywordId") Long adMzKeywordId, @Param("adMzCookieId") Long adMzCookieId, @Param("hour") Long hour);

    List<ClickiMzAdBase> selectByDate(@Param("date") Long date);

    List<ClickiMzAdBase> selectSpots(@Param("date") Long date);

    int updateByPrimaryKey(ClickiMzAdBase record);
}