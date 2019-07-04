package com.weixin.mp.demo.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class WxMpReply implements Serializable {

    private String touser;
    private String template_id;
    private String url;
    private WxModelData data;

}
