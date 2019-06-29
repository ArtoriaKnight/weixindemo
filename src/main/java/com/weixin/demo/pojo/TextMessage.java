package com.weixin.demo.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class TextMessage {

    private String content;
    private Date createTime;
    private String toUserName;
    private String fromUserName;
    private Long msgId;
    private String msgType;

}
