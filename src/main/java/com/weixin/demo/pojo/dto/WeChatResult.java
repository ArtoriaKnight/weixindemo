package com.weixin.demo.pojo.dto;

import lombok.Data;

@Data
public class WeChatResult {

    private Object object;
    private Integer type;

    public boolean isSuccess() {
        return true;
    }
}
