package com.wechat4j.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.weixin.demo.constant.WechatConstant;
import com.weixin.demo.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.weixin4j.loader.ITicketLoader;
import org.weixin4j.model.js.Ticket;
import org.weixin4j.model.js.TicketType;

@Slf4j
@Component
public class RedisTicketLoader implements ITicketLoader {

    @Autowired
    private RedisUtil redisUtil;

    @Value("${weixin4j.config.appid}")
    private String appid;

    @Override
    public Ticket get(TicketType ticketType) {
        String key = "";
        if (ticketType !=null) {
            key = getKey(ticketType);
            String result = redisUtil.getString(key,1);
            log.info("wechat ticket: {}", result);
            return JSON.parseObject(result,Ticket.class);
        }
        return null;
    }

    private String getKey(TicketType ticketType) {
        String key;
        switch (ticketType) {
            case JSAPI:
                key = WechatConstant.TICKET_JSAPI;
            case WX_CARD:
                key = WechatConstant.TICKET_WXCARD;
            default:
                key = WechatConstant.TICKET_DEFAULT;
                break;
        }
        return key;
    }

    @Override
    public void refresh(Ticket ticket) {
        String key = "";
        if (null != ticket) {
            key = getKey(ticket.getTicketType());
        }
        log.info("refresh wechat ticket");
        String ticketStr = JSON.toJSONString(ticket);

        redisUtil.setStringWithExpire(key,ticketStr,1,ticket.getExpires_in() - 600);

    }
}
