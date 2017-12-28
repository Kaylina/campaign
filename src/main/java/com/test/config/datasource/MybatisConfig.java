package com.test.config.datasource;

import com.test.common.constant.ApplicationProperties;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: lemon
 * @Time: 2017/6/6 15:41
 * @Describe: MapperScannerConfigurer 配置
 */
@Configuration
public class MybatisConfig {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //bask package
    private static final String BASE_PACKAGE = ApplicationProperties.getString("mybatis.base-package");

    /**
     * Created with lemon
     * Time: 2017/7/16 22:25
     * Description: mapperScannerConfigurer 实例化
     */
    @Bean(name = "mapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        logger.info("mapperScannerConfigurer init...");
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(BASE_PACKAGE);
        return mapperScannerConfigurer;
    }
}
