package com.wechat4j.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weixin.demo.constant.WechatConstant;
import com.weixin.demo.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.weixin4j.loader.ITokenLoader;
import org.weixin4j.model.base.Token;

@Slf4j
@Component
public class RedisTokenLoader implements ITokenLoader {

    @Value("${weixin4j.config.appid}")
    private String appid;

    @Autowired
    private RedisUtil redisUtil;

    public RedisTokenLoader(String appid){
        this.appid = appid;
    }

    @Override
    public Token get() {
        String accessToken = redisUtil.getString(WechatConstant.ACCESS_TOKEN_KEY,1);
        log.info("wechat access_token:{}", accessToken);

        return JSON.parseObject(accessToken, Token.class);
    }

    @Override
    public void refresh(Token token) {
        log.info("wechat refresh token: {}", token.toString());
        String accessToken = JSON.toJSONString(token);
        redisUtil.setStringWithExpire(WechatConstant.ACCESS_TOKEN_KEY, accessToken,1,token.getExpires_in()-600);

    }
}
