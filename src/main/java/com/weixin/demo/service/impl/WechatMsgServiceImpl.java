package com.weixin.demo.service.impl;

import com.weixin.demo.pojo.BaseMessage;
import com.weixin.demo.pojo.TextMessage;
import com.weixin.demo.pojo.dto.WeChatResult;
import com.weixin.demo.service.WechatMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class WechatMsgServiceImpl implements WechatMsgService {

    /** author hjy
     *  descr 回复文本消息 */
    @Override
    public WeChatResult textMsgInfo(Map<String, String> params, BaseMessage msgInfo) {
        log.info("文本消息回复");
        WeChatResult result = new WeChatResult();
        TextMessage text = new TextMessage();
        text.setContent(params.get("Content").trim());// 自动回复
        text.setCreateTime(new Date());
        text.setToUserName(msgInfo.getFromUserName());
        text.setFromUserName(msgInfo.getToUserName());
        text.setMsgId(msgInfo.getMsgId());
        text.setMsgType("text");
        result.setObject(text);
        return result;
    }

}
