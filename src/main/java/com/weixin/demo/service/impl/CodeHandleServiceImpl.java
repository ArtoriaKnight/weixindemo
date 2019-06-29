package com.weixin.demo.service.impl;

import com.weixin.demo.pojo.BaseMessage;
import com.weixin.demo.pojo.dto.WeChatResult;
import com.weixin.demo.service.CodeHandleService;
import com.weixin.demo.service.WechatMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CodeHandleServiceImpl implements CodeHandleService {

    @Autowired
    private WechatMsgService wechatMsgService;


    @SuppressWarnings("ConstantConditions")
    @Override
    public WeChatResult handleCode(Map<String, String> params, BaseMessage msgInfo) {
        WeChatResult result = new WeChatResult();

        String msgInfoType = params.get("MsgType");
        return result;
    }
}
