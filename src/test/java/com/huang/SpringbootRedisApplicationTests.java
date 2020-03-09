package com.huang;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class SpringbootRedisApplicationTests {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        redisTemplate.opsForValue().set("stockA",500+"");
        redisTemplate.opsForValue().set("stockB",500+"");
        redisTemplate.opsForValue().set("stockC",500+"");
        redisTemplate.opsForValue().set("stockD",500+"");
        redisTemplate.opsForValue().set("stockE",500+"");
        System.out.println("stock add 500 success!");
    }

}
