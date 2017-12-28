package com.test.config.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author: lemon
 * @Time: 2017/6/29 15:33
 * @Describe: SchedulingConfigurer 配置(task配置)
 */
@Configuration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Created with lemon
     * Time: 2017/7/16 22:24
     * Description: SchedulingConfigurer初始化
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        logger.info("SchedulingConfigurer init...");
        taskRegistrar.setScheduler(taskExecutor());
    }

    /**
     * Created with lemon
     * Time: 2017/7/16 22:24
     * Description: 定时任务线程池初始化
     */
    @Bean(name="scheduledThreadPoolExecutor",destroyMethod="shutdown")
    public Executor taskExecutor() {
        logger.info("scheduledThreadPoolExecutor init...");
        return Executors.newScheduledThreadPool(20);
    }
}