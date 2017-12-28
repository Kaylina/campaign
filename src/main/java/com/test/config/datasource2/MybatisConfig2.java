package com.test.config.datasource2;

import com.test.common.constant.ApplicationProperties;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: jingyan
 * @Time: 2017/8/30 15:50
 * @Describe:
 */
@Configuration
public class MybatisConfig2 {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //bask package
    private static final String BASE_PACKAGE2 = ApplicationProperties.getString("mybatis.base-package2");

    /**
     * Created with lemon
     * Time: 2017/7/16 22:25
     * Description: mapperScannerConfigurer 实例化
     */
    @Bean(name = "mapperScannerConfigurer2")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        logger.info("mapperScannerConfigurer init...");
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory2");
        mapperScannerConfigurer.setBasePackage(BASE_PACKAGE2);
        return mapperScannerConfigurer;
    }
}
