## 本项目运行环境为jdk1.7, redis2.8(d14,6344端口),mysql(d162,3610端口)，springBoot框架。
##### 1、Info库查询近一个月更新过的campaign_id，循环所有campaign_id调用Adlemon报表接口；
##### 2、分别以campaign和spot两个维度获取PV、UV、Click、Clicker等信息生成csv文件；
##### 3、java代码生成csv文件，shell脚本把csv文件提交到hdfs和druid。
##### 4、两个定时任务，一个每天凌晨1点执行一次跑前一天的广告数据，一个补数，日期是从redis中动态获取的key。项目设计是完全自动化的，如果需要跑特定某天的数据，可手动修改redis中对应key值。