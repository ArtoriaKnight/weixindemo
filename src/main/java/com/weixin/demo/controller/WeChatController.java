package com.weixin.demo.controller;

import com.weixin.demo.service.MessageHandleService;
import com.weixin.demo.util.MessageUtil;
import com.weixin.demo.util.SignatureUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
public class WeChatController extends BaseController {

    @Autowired
    private MessageHandleService messageHandleService;

    /**
     * 校验信息是否是从微信服务器发出，处理消息
     *
     * @throws Exception
     */
    @RequestMapping(value = "/handler", method = { RequestMethod.GET, RequestMethod.POST })
    public void processPost() throws Exception {

        this.getRequest().setCharacterEncoding("UTF-8");
        this.getResponse().setCharacterEncoding("UTF-8");

        log.info("开始校验信息是否是从微信服务器发出");
        boolean ispost = Objects.equals("POST", this.getRequest().getMethod().toUpperCase());

        if (ispost){
            log.info("接入成功, 开始处理逻辑");
            // 从 request 获取流
            InputStream inputStream = this.getRequest().getInputStream();
            Map<String,String> params = MessageUtil.parseXml(inputStream);

            String respXml = messageHandleService.handleMessage(params);
            if (StringUtils.isNotEmpty(respXml)){
                // 输出流
                this.getResponse().getWriter().write(respXml);
            }

        } else {
            // 签名
            String signature = this.getRequest().getParameter("signature");
            // 时间戳
            String timestamp = this.getRequest().getParameter("timestamp");
            // 随机数
            String nonce = this.getRequest().getParameter("nonce");
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败

            if (SignatureUtil.checkSignature(signature, timestamp, nonce)) {
                // 随机字符串
                String echostr = this.getRequest().getParameter("echostr");
                log.debug("接入成功，echostr {}", echostr);
                this.getResponse().getWriter().write(echostr);
            }
        }
    }

}
