package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class Application {

    /**
     * Created with lemon
     * Time: 2017/7/16 21:44
     * Description: APP入口
     */

    public static void main(String[] args) {

        System.out.println("Application is start...");
        SpringApplication.run(Application.class, args);
    }

}
