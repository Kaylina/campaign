package com.test.config.datasource2;

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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @Author: jingyan
 * @Time: 2017/8/30 15:47
 * @Describe:
 */
@Configuration
@ConditionalOnClass(DruidDataSource.class)                  //存在DruidDataSource的类才创建Bean
@ConditionalOnProperty(prefix = "druid", name = "url2")      //存在url才创建下面的Bean
@AutoConfigureBefore(DataSourceAutoConfiguration.class)     //在DataSourceAutoConfiguration
@EnableConfigurationProperties(DruidProperties2.class)       //注入配置文件
@EnableTransactionManagement
public class DruidConfig2 {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String MAPPER_LOCATIONS2 = ApplicationProperties.getString("mybatis.mapper-locations2");
    @Autowired
    private DruidProperties2 druidProperties2;

    /**
     * @Author: lemon
     * @Time: 2017/6/5 14:36
     * @Describe: 数据源
     */
    @Bean(name = "dataSource2")
    public DataSource dataSource() {
        logger.info("druid initialization：/n" + druidProperties2.toString());
        DruidDataSource dataSource = new DruidDataSource();
        try {
            dataSource.setUrl(druidProperties2.getUrl2());
            dataSource.setUsername(druidProperties2.getUsername2());
            dataSource.setPassword(druidProperties2.getPassword2());
            dataSource.setInitialSize(druidProperties2.getInitialSize() == 0 ? 1 : druidProperties2.getInitialSize());
            dataSource.setMaxActive(druidProperties2.getMaxActive() == 0 ? 20 : druidProperties2.getMaxActive());
            dataSource.setMinIdle(druidProperties2.getMinIdle() == 0 ? 1 : druidProperties2.getMinIdle());
            dataSource.setMaxWait(druidProperties2.getMaxWait() == 0 ? 60000 : druidProperties2.getMaxWait());
            dataSource.setTimeBetweenEvictionRunsMillis(druidProperties2.getTimeBetweenEvictionRunsMillis());
            dataSource.setMinEvictableIdleTimeMillis(druidProperties2.getMinEvictableIdleTimeMillis());
            dataSource.setValidationQuery(druidProperties2.getValidationQuery());
            dataSource.setTestWhileIdle(druidProperties2.isTestWhileIdle());
            dataSource.setTestOnBorrow(druidProperties2.isTestOnBorrow());
            dataSource.setTestOnReturn(druidProperties2.isTestOnReturn());
            dataSource.setRemoveAbandoned(druidProperties2.isRemoveAbandoned());
            dataSource.setRemoveAbandonedTimeoutMillis(druidProperties2.getRemoveAbandonedTimeout() == 0 ? 1800 : druidProperties2.getMaxWait());
            dataSource.setLogAbandoned(druidProperties2.isLogAbandoned());
            dataSource.setFilters(druidProperties2.getFilters());
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
    @Bean(name = "sqlSessionFactory2")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource2") DataSource dataSource2) throws Exception {
        logger.info("sqlSessionFactory init...");
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource2);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATIONS2));
        return sessionFactory.getObject();
    }

    /**
     * @Author: lemon
     * @Time: 2017/6/5 14:36
     * @Describe: 事务
     */
    @Bean(name = "transactionManager2")
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource2") DataSource dataSource2) {
        logger.info("transactionManager init...");
        return new DataSourceTransactionManager(dataSource2);
    }
}
