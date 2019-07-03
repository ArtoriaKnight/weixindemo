package com.weixin.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis 数据库连接池
 */
@Component
public abstract class JedisConnectPool {

    protected static JedisPool jcpool = null;
    protected Integer RANDOM_DB_NUMBER; // 这些随机值存放的数据库，15

    @Autowired
    protected Environment env;

    protected void check() {
        if (jcpool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setBlockWhenExhausted(true);
            config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
            config.setJmxEnabled(false);
            config.setLifo(true);
            config.setMaxIdle(10);
            config.setMaxTotal(2000);
            config.setMaxWaitMillis(200);
            config.setMinEvictableIdleTimeMillis(1800000);
            config.setMinIdle(5);
            config.setNumTestsPerEvictionRun(3);
            config.setSoftMinEvictableIdleTimeMillis(1800000);
            config.setTestOnBorrow(false);
            config.setTestWhileIdle(false);
            config.setTimeBetweenEvictionRunsMillis(-1);
            jcpool = new JedisPool(config, "127.0.0.1",
                    6379, 3000);
//            jcpool = new JedisPool(config, redisIP, redisPort, 3000);
            RANDOM_DB_NUMBER = 15;
        }
    }

}
