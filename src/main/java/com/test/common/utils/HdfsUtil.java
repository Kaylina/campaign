package com.test.common.utils;

import com.test.common.constant.ApplicationProperties;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;

public class HdfsUtil {
    private static Logger logger = LoggerFactory.getLogger(HdfsUtil.class);

    public static boolean uploadFile(String uploadFile) {
        try {
            Configuration conf = new Configuration();
            URI uri = new URI(ApplicationProperties.getString("hdfs.url"));
            //URI uri = new URI("hdfs://10.200.238.145:9000");
            FileSystem fs = FileSystem.get(uri, conf);
            Path filePath = new Path(uploadFile);
            Path hdfsPath = new Path("/druid/csv");
            File file = new File(uploadFile);
            if (file.exists()) {
                if (!fs.exists(hdfsPath)) {
                    fs.mkdirs(hdfsPath);
                }
                fs.copyFromLocalFile(filePath, hdfsPath);
                fs.close();
                logger.info(uploadFile + " put to hdfs success ...");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        uploadFile("E:/data/www/campaign/json/spot.json");

    }
}
