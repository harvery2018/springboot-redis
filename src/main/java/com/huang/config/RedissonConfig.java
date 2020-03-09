package com.huang.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class RedissonConfig {

    //单机版 Redisson
//    @Bean
//    public Redisson redisson(){
//        Config config=new Config();
//        config.useSingleServer().setAddress("redis://39.108.248.8").setDatabase(0);
//        return (Redisson) Redisson.create(config);
//    }





}
