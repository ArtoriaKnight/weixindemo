package com.weixin.mp.demo.config;

import com.weixin.mp.demo.utils.JsonUtils;
import lombok.Data;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * wechat mp properties
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@Data
@Component
public class WxMpProperties {
    private List<MpConfig> configs;

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
