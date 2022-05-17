package com.px.config.db;

import com.px.core.util.RedisHelper;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * @author zhouz
 * @Date 2021/7/7 18:17
 * @Description
 */
@EnableCaching
@Configuration
public class RedisConfig {

    private final LettuceConnectionFactory factory;

    public RedisConfig(LettuceConnectionFactory factory) {
        this.factory = factory;
    }

    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean
    public RedisHelper redisHelper(RedisTemplate<String, Serializable> redisTemplate, RedisAutoConfiguration redisConfig) {
        return this.redisHelper(this.factory, redisTemplate, redisConfig);
    }

    public RedisHelper redisHelper(LettuceConnectionFactory factory, RedisTemplate<String, Serializable> redisTemplate, RedisAutoConfiguration redisConfig) {
        return new RedisHelper(redisTemplate);
    }
}
