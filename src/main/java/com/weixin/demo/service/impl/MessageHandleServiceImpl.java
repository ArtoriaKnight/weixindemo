package com.weixin.demo.service.impl;

import com.weixin.demo.service.MessageHandleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class MessageHandleServiceImpl implements MessageHandleService {

    /*
     * 对来自微信的消息作出响应(包含消息和事件)
     * @param inputStream
     * @return
     * @throws Exception
     */
    @Override
    public String handleMessage(Map<String, String> map) {
        log.info("开始处理[message]信息");
        String result = null;

        return null;
    }
}
