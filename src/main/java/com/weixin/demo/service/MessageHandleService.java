package com.weixin.demo.service;

import java.util.Map;

public interface MessageHandleService {

    String handleMessage(Map<String, String> map) throws Exception;
}
