package com.weixin.mp.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.weixin.mp.demo.handler.TokenHandler;
import com.weixin.mp.demo.pojo.vo.WxModel;
import com.weixin.mp.demo.pojo.vo.WxModelData;
import com.weixin.mp.demo.pojo.vo.WxMpReply;
import com.weixin.mp.demo.pojo.vo.WxReplyMessage;
import com.weixin.mp.demo.utils.HttpClientUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("wx/portal/{appid}")
public class WxPortalController {
    private final WxMpService wxService;
    private final WxMpMessageRouter messageRouter;
    private final TokenHandler tokenHandler;


    @GetMapping("test")
    public String apiTest(){
        return tokenHandler.getAccessToken();
    }

    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(@PathVariable String appid,
                          @RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {

        log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature,
            timestamp, nonce, echostr);
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }

        if (!this.wxService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        if (wxService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }

        return "非法请求";
    }

    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@PathVariable String appid,
                       @RequestBody String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam("openid") String openid,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        log.info("\n接收微信请求：openid=[{}], signature=[{}], encType=[{}], msgSignature=[{}],"
                + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
            openid, signature, encType, msgSignature, timestamp, nonce, requestBody);

        if (!this.wxService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        if (!wxService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        String out = null;
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);

            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toXml();
        } else if ("aes".equalsIgnoreCase(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxService.getWxMpConfigStorage(),
                timestamp, nonce, msgSignature);
            log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
        }

        log.info("\n组装回复信息：{}", out);

        return out;
    }

    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return this.messageRouter.route(message);
        } catch (Exception e) {
            log.error("路由消息时出现异常！", e);
        }

        return null;
    }

    @GetMapping("send/test")
    public String sendMessage(){
        String result = "";
//        String sendUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+tokenHandler.getAccessToken();
        String modelUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+tokenHandler.getAccessToken();
//        WxMpXmlOutTextMessage mpXmlOutTextMessage = new WxMpXmlOutTextMessage();
//        mpXmlOutTextMessage.setContent("4565768765");
//        mpXmlOutTextMessage.setFromUserName("gh_92e2da250c64");
//        mpXmlOutTextMessage.setToUserName("oHkLR5yXxgJ3MVf-Nd5VlLJprLLo");
//        mpXmlOutTextMessage.setMsgType("text");
//        mpXmlOutTextMessage.setCreateTime(new Date().getTime());

        WxMpReply reply = new WxMpReply();
        WxModelData modelData = new WxModelData();
        WxModel model1 = new WxModel();
        model1.setValue("恭喜购买成功");
        model1.setColor("#173177");
        WxModel model2 = new WxModel();
        model2.setValue("巧克力");
        model2.setColor("#173177");
        WxModel model3 = new WxModel();
        model3.setValue("39.8元");
        model3.setColor("#173177");
        WxModel model4 = new WxModel();
        model4.setValue("2014年9月22日");
        model4.setColor("#173177");
        WxModel model5 = new WxModel();
        model5.setValue("欢迎再次购买！");
        model5.setColor("#173177");
        modelData.setFirst(model1);
        modelData.setKeyword1(model2);
        modelData.setKeyword2(model3);
        modelData.setKeyword3(model4);
        modelData.setRemark(model5);
        reply.setTouser("oKIp10rO9o7V4QWYt3hPW0gHgL5M");
        reply.setUrl("www.baidu.com");
        reply.setData(modelData);
        reply.setTemplate_id("zM9FHmvf0l-M7orAjDT7c2eMZplHSygF73A2m-OB1ow");

        String jsonTestMessage = JSONObject.toJSONString(reply);
//        XStream xStream = new XStream();
//        xStream.alias("xml",inMessage.getClass());

//        String jsonTestMessage = xStream.toXML(inMessage);
//        String jsonTestMessage = mpXmlOutTextMessage.toXml();
//        String jsonTestMessage = String.format(
//                        "<xml>" +
//                                "<ToUserName><![CDATA[%s]]></ToUserName>" +
//                                "<FromUserName><![CDATA[%s]]></FromUserName>" +
//                                "<CreateTime>%s</CreateTime>" +
//                                "<MsgType><![CDATA[text]]></MsgType>" +
//                                "<Content><![CDATA[%s]]></Content>" +
//                                "</xml>",
//                        "oHkLR5yXxgJ3MVf-Nd5VlLJprLLo", "o99_X1QouzKQgilJjhIcc6DqRb50", new Date().getTime(),
//                        "4565768765");

        int count = 1;
        while(true){
            try{
                result = HttpClientUtils.sendPost(modelUrl,jsonTestMessage);
                JSONObject json = JSON.parseObject(result);
                if(!"0".equals(json.getString("errcode"))){
                    System.out.println("\n"+result+"\n");
                    throw new Exception("发送失败");
                }else{
                    System.out.println("success");
                    break;
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                if(count >= 3){
                    break;
                }
                count++;
            }
        }

        return jsonTestMessage;
    }

}
