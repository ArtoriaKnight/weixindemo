package com.weixin.mp.demo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@Data
@PropertySource(value = "classpath:application.yml")
@ConfigurationProperties(prefix = "wx.mp.configs")
public class MpConfig {

    /**
     * 设置微信公众号的appid
     */
    private String appId="wx11007010f397b41b";

    /**
     * 设置微信公众号的app secret
     */
    private String secret="3d75c5367406e6be8030d9cab310b911";

    /**
     * 设置微信公众号的token
     */
    private String token="authtoken";

    /**
     * 设置微信公众号的EncodingAESKey
     */
    private String aesKey="uFgeSY9aippul81SHPNjfo5QWpD6wdwmPcJo9gSkHlc";
}
