package com.test.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * @Author: lemon
 * @Time: 2017/7/27 10:51
 * @Describe:
 */
public class FileUtil {

    public static String createJsonFile(String jsonString, String filePath, String fileName) {
        // 拼接文件完整路径
        String fullPath = filePath + File.separator + fileName;
        // 生成json格式文件
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();
            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsonString);
            write.flush();
            write.close();
            return fullPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
