package com.weixin.demo.service.impl;

import com.weixin.demo.pojo.BaseMessage;
import com.weixin.demo.pojo.dto.WeChatResult;
import com.weixin.demo.service.CodeHandleService;
import com.weixin.demo.service.MessageHandleService;
import com.weixin.demo.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class MessageHandleServiceImpl implements MessageHandleService {

    @Autowired
    private CodeHandleService codeHandleService;
    /*
     * 对来自微信的消息作出响应(包含消息和事件)
     * @param inputStream
     * @return
     * @throws Exception
     */
    @Override
    public String handleMessage(Map<String, String> params) throws Exception {
        log.info("开始处理[message]信息");
        String result = null;
        if (params != null && params.size() > 0) {
            BaseMessage msgInfo = new BaseMessage();
            String createTime = params.get("CreateTime");
            String msgId = params.get("MsgId");
            msgInfo.setCreateTime((createTime != null && !"".equals(createTime)) ? Long.parseLong(createTime) : 0L);
            msgInfo.setFromUserName(params.get("FromUserName"));
            msgInfo.setMsgId((msgId != null && !"".equals(msgId)) ? Long.parseLong(msgId) : 0L);
            msgInfo.setToUserName(params.get("ToUserName"));
            WeChatResult resp = codeHandleService.handleCode(params, msgInfo);
            if (null == resp) {
//              String str = "<xml>\r\n" + "  <ToUserName><![CDATA[oWnU700PB2HTtgzReJva2Gz00000]]></ToUserName>\r\n"
//                      + "  <FromUserName><![CDATA[gh_23ba766d4f8c]]></FromUserName>\r\n"
//                      + "  <CreateTime><![CDATA[1534324363936]]></CreateTime>\r\n"
//                      + "  <MsgType><![CDATA[news]]></MsgType>\r\n" + "  <FuncFlag><![CDATA[0]]></FuncFlag>\r\n"
//                      + "  <ArticleCount><![CDATA[2]]></ArticleCount>\r\n" + "  <Articles>\r\n" + "    <item>\r\n"
//                      + "      <Title><![CDATA[飞狐互动：feefox]]></Title>\r\n"
//                      + "      <Description><![CDATA[欢迎关注我的个人博客<a href=\"http://118.25.62.232/images/showreel.jpg\">图片</a><a href=\"http://118.25.62.232/images/showreel.jpg\">图片</a>]]></Description>\r\n"
//                      + "      <PicUrl><![CDATA[http://118.25.62.232/images/showreel.jpg]]></PicUrl>\r\n"
//                      + "      <Url><![CDATA[http://118.25.62.232/apple/]]></Url>\r\n" + "    </item>\r\n"
//                      + "    <item>\r\n" + "      <Title><![CDATA[飞狐互动：feefox]]></Title>\r\n"
//                      + "      <Description><![CDATA[欢迎关注我的个人博客<a href=\"http://118.25.62.232/images/showreel.jpg\">图片</a><a href=\"http://118.25.62.232/images/showreel.jpg\">图片</a>]]></Description>\r\n"
//                      + "      <PicUrl><![CDATA[http://118.25.62.232/images/showreel.jpg]]></PicUrl>\r\n"
//                      + "      <Url><![CDATA[http://118.25.62.232/apple/]]></Url>\r\n" + "    </item>\r\n"
//                      + "  </Articles>\r\n" + "</xml>";
//              return str;
                return null;
            }
            boolean success = resp.isSuccess(); // 如果 为true,则表示返回xml文件, 直接转换即可,否则按类型
            if (success) {
                result = resp.getObject().toString();
            } else {
                int type = resp.getType(); // 这里规定 1 图文消息 否则直接转换
//                if (type == WeChatResult.NEWSMSG) {
//                    NewsMessage message = (NewsMessage) resp.getObject();
//                    result = MessageUtil.newsMessagegToXml(message);
//                    return result;
//                } else {
//                    BaseMessage baseMessage = (BaseMessage) resp.getObject();
//                    result = MessageUtil.baseMessageToXml(baseMessage);
//                    return result;
//                }
            }
        } else {
            result = "msg is wrong";

        }
        return result;
    }
}
