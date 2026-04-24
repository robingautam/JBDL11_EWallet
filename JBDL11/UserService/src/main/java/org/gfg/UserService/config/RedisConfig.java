package org.gfg.UserService.config;

import org.gfg.UserService.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {


    @Bean
    public RedisTemplate<String,String> otpRedisTemplate(){
        RedisTemplate<String,String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
        lettuceConnectionFactory.setHostName("redis-10232.crce281.ap-south-1-3.ec2.cloud.redislabs.com");
        lettuceConnectionFactory.setPassword("STigqEYQQWJZ520rBW26LRz30vFjyJFp");
        lettuceConnectionFactory.setPort(10232);
        lettuceConnectionFactory.start();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        return  redisTemplate;
    }


}
