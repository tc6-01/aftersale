package com.abc.aftersale.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

/**
 * @author zhaoranzhi
 * @create 2024-05-24-12:54
 */
@Configuration
public class JedisConfig {
    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private Integer port;

    @Bean
    public Jedis jedis() {
        Jedis jedis = new Jedis(host, port);
        return jedis;
    }
}
