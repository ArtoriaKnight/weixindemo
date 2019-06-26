package com.weixin.demo.pojo;

import lombok.Data;

@Data
public class BaseMessage {

    private String toUserName;
    private String fromUserName;
    private String msgId;
    private Long createTime;

}
