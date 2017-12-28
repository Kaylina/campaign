package com.test.adCampaign.service;

import com.test.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by lemon on 2017/7/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class AuthServiceTest {

    @Resource
    private AuthService authService;

    @Test
    public void testTask() {

        authService.getToken();
    }

}