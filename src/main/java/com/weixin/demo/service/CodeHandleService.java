package com.weixin.demo.service;

import com.weixin.demo.pojo.BaseMessage;
import com.weixin.demo.pojo.dto.WeChatResult;

import java.util.Map;

public interface CodeHandleService {

    WeChatResult handleCode(Map<String, String> params, BaseMessage msgInfo);
}
