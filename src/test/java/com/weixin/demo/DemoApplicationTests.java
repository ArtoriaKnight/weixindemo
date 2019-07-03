package com.weixin.demo;

import com.weixin.mp.demo.handler.TokenHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplicationTests.class)
public class DemoApplicationTests {


    @Test
    public void contextLoads() {
        TokenHandler tokenHandler = new TokenHandler();
        String token = tokenHandler.getAccessToken();
        System.out.println("调用成功, token = "+token);

    }

}
