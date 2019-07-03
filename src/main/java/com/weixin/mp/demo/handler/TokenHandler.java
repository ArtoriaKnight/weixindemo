package com.weixin.mp.demo.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weixin.mp.demo.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxAccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class TokenHandler {
    @Autowired
    private WxMpService wxService;
    @Autowired
    private RedisUtil redisUtil;

    public static JSONObject doGetstr(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        JSONObject jsonObject = null;
        try {
            CloseableHttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                jsonObject = JSON.parseObject(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject doPoststr(String url,String outStr) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonObject = null;
        try {
            httpPost.setEntity(new StringEntity(outStr, "utf-8"));
            CloseableHttpResponse response = httpclient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), "utf-8");
            jsonObject = JSON.parseObject(result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }

    public WxAccessToken setAccessToken(){
        System.out.println("从接口中获取");
        WxAccessToken token = new WxAccessToken();


        String url = String.format(WxMpService.GET_ACCESS_TOKEN_URL,
                wxService.getWxMpConfigStorage().getAppId(), wxService.getWxMpConfigStorage().getSecret());
        JSONObject json = doGetstr(url);
        if(json!=null){
            token.setAccessToken(json.getString("access_token"));
            token.setExpiresIn(json.getInteger("expires_in"));
            redisUtil.setStringWithExpire("access_token",json.getString("access_token"),1,7000);
        }
        return token;
    }

    /**
     * 获取凭证
     * @return
     */
    public String  getAccessToken(){
        System.out.println("从缓存中读取");
        String access_token = redisUtil.getString("access_token",1);
        if(access_token==null){
            WxAccessToken token = setAccessToken();
            access_token = token.getAccessToken();
        }
        return access_token;
    }


}
