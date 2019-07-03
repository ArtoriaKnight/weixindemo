package com.weixin.mp.demo.pojo.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import lombok.Data;
import me.chanjar.weixin.common.util.xml.XStreamCDataConverter;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.util.crypto.WxMpCryptUtil;
import me.chanjar.weixin.mp.util.xml.XStreamTransformer;

import java.io.Serializable;

@XStreamAlias("xml")
@Data
public class WxReplyMessage implements Serializable {

    @XStreamAlias("Content")
    @XStreamConverter(value = XStreamCDataConverter.class)
    public String Content;

    @XStreamAlias("ToUserName")
    @XStreamConverter(value = XStreamCDataConverter.class)
    public String ToUserName;

    @XStreamAlias("FromUserName")
    @XStreamConverter(value = XStreamCDataConverter.class)
    public String FromUserName;

    @XStreamAlias("CreateTime")
    public Long CreateTime;

    @XStreamAlias("MsgType")
    @XStreamConverter(value = XStreamCDataConverter.class)
    public String MsgType;


    @SuppressWarnings("unchecked")
    public String toXml() {
        return XStreamTransformer.toXml((Class<WxReplyMessage>) this.getClass(), this);
    }

    /**
     * 转换成加密的xml格式
     */
    public String toEncryptedXml(WxMpConfigStorage wxMpConfigStorage) {
        String plainXml = toXml();
        WxMpCryptUtil pc = new WxMpCryptUtil(wxMpConfigStorage);
        return pc.encrypt(plainXml);
    }

}
