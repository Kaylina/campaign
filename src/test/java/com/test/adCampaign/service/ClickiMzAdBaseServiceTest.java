package com.test.adCampaign.service;

import com.test.Application;
import com.test.adCampaign.clickiMzAdBase.service.ClickiMzAdBaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by lemon on 2017/9/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ClickiMzAdBaseServiceTest {
    @Resource
    private ClickiMzAdBaseService clickiMzAdBaseService;

    @Test
    public void testTask() {

        clickiMzAdBaseService.getSpots("2017-09-10");
        clickiMzAdBaseService.getSpotsMap(clickiMzAdBaseService.getSpots("2017-09-10"));
    }
}