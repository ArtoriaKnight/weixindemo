package com.weixin.demo.controller;

import com.weixin.demo.util.SignatureUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
public class WeChatController extends BaseController {


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
