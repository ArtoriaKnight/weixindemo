package com.weixin.mp.demo.utils;

import com.weixin.demo.util.JedisConnectPool;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class RedisUtil extends JedisConnectPool {


    public Jedis getRedisClient(){
        check();

        Jedis redisClient = null;
        try {
            redisClient = jcpool.getResource();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return redisClient;
    }

    // 指定获取 redis缓存库
    public Jedis getRedisClient(int dbNum){
        check();
        Jedis redisClient = null;
        try {
            redisClient = jcpool.getResource();
            redisClient.select(dbNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return redisClient;
    }

    /*** 获得 string 类型的 value */
    public String getString(String key, int dbNum){
        check();
        Jedis redisClient = null;
        String result = "";
        try {
            redisClient = jcpool.getResource();
            redisClient.select(dbNum);
            result = redisClient.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /*** 插入一个 string 类型的 value */
    public boolean setString(String key, String value, int dbNum){
        check();
        Jedis redisClient = null;
        try {
            redisClient = jcpool.getResource();
            redisClient.select(dbNum);
            String result = redisClient.set(key,value);
            return result != null && "OK".equals(result);
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /*** 插入一个 string 类型的 value */
    public Long setStringWithExpire(String key, String value, int dbNum, int seconds){
        check();
        Jedis redisClient = null;
        try {
            redisClient = jcpool.getResource();
            redisClient.select(dbNum);
            String result = redisClient.set(key,value);
            if (result != null && "OK".equals(result))
                return redisClient.expire(key,seconds);
        } catch (Exception e){
            e.printStackTrace();
        }
        return -1L;
    }

}
