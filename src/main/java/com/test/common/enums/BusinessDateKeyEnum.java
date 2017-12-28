package com.test.common.enums;

/**
 * @Author: lemon
 * @Time: 2017/7/28 16:00
 * @Describe:
 */
public enum BusinessDateKeyEnum {

    BACK_BUSINESS_DATE_KEY("adm_reloadDate", "补数业务日期key"),
    FORWARD_BUSINESS_DATE_KEY("adm_businessDate", "正常业务日期key");

    private String redisKey;
    private String keyDescribetion;


    BusinessDateKeyEnum(String redisKey, String keyDescribetion) {
        this.redisKey = redisKey;
        this.keyDescribetion = keyDescribetion;
    }


    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public String getKeyDescribetion() {
        return keyDescribetion;
    }

    public void setKeyDescribetion(String keyDescribetion) {
        this.keyDescribetion = keyDescribetion;
    }
}
