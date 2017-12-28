package com.test.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.test.common.constant.ApplicationProperties;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @Author: lemon
 * @Time: 2017/6/5 11:31
 * @Describe: 数据库连接池
 */
@Configuration
@ConditionalOnClass(DruidDataSource.class)                  //存在DruidDataSource的类才创建Bean
@ConditionalOnProperty(prefix = "druid", name = "url")      //存在url才创建下面的Bean
@AutoConfigureBefore(DataSourceAutoConfiguration.class)     //在DataSourceAutoConfiguration
@EnableConfigurationProperties(DruidProperties.class)       //注入配置文件
@EnableTransactionManagement
public class DruidConfig {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //mapper locations
    private static final String MAPPER_LOCATIONS = ApplicationProperties.getString("mybatis.mapper-locations");
    @Autowired
    private DruidProperties druidProperties;

    /**
     * @Author: lemon
     * @Time: 2017/6/5 14:36
     * @Describe: 数据源
     */
    @Bean(name = "dataSource")
    @Primary
    public DataSource dataSource() {
        logger.info("druid initialization：/n" + druidProperties.toString());
        DruidDataSource dataSource = new DruidDataSource();
        try {
            dataSource.setUrl(druidProperties.getUrl());
            dataSource.setUsername(druidProperties.getUsername());
            dataSource.setPassword(druidProperties.getPassword());
            dataSource.setInitialSize(druidProperties.getInitialSize() == 0 ? 1 : druidProperties.getInitialSize());
            dataSource.setMaxActive(druidProperties.getMaxActive() == 0 ? 20 : druidProperties.getMaxActive());
            dataSource.setMinIdle(druidProperties.getMinIdle() == 0 ? 1 : druidProperties.getMinIdle());
            dataSource.setMaxWait(druidProperties.getMaxWait() == 0 ? 60000 : druidProperties.getMaxWait());
            dataSource.setTimeBetweenEvictionRunsMillis(druidProperties.getTimeBetweenEvictionRunsMillis());
            dataSource.setMinEvictableIdleTimeMillis(druidProperties.getMinEvictableIdleTimeMillis());
            dataSource.setValidationQuery(druidProperties.getValidationQuery());
            dataSource.setTestWhileIdle(druidProperties.isTestWhileIdle());
            dataSource.setTestOnBorrow(druidProperties.isTestOnBorrow());
            dataSource.setTestOnReturn(druidProperties.isTestOnReturn());
            dataSource.setRemoveAbandoned(druidProperties.isRemoveAbandoned());
            dataSource.setRemoveAbandonedTimeoutMillis(druidProperties.getRemoveAbandonedTimeout() == 0 ? 1800 : druidProperties.getMaxWait());
            dataSource.setLogAbandoned(druidProperties.isLogAbandoned());
            dataSource.setFilters(druidProperties.getFilters());
            dataSource.init();
            logger.info("druid initialization success...");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataSource;
    }
    /**
     * @Author: lemon
     * @Time: 2017/6/5 14:36
     * @Describe: 连接池
     */
    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        logger.info("sqlSessionFactory init...");
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATIONS));
        return sessionFactory.getObject();
    }

    /**
     * @Author: lemon
     * @Time: 2017/6/5 14:36
     * @Describe: 事务
     */
    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        logger.info("transactionManager init...");
        return new DataSourceTransactionManager(dataSource);
    }

}
