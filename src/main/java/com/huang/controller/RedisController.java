package com.huang.controller;

import com.huang.utils.ApplicationContextProvider;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.redisson.Redisson.create;

@RestController
public class RedisController {

    @Autowired
    private StringRedisTemplate redisTemplate;

//    @Autowired
//    @Qualifier("testReidssion")
//    private Redisson redisson;



    /**
     * 使用Redisson锁（单机版使用）
     * @return
     */
    @RequestMapping("/deduct_stockE")
    public String deductStockE() throws InterruptedException {
        String lockKey="lockKey";
        String clientId= UUID.randomUUID().toString();


        //redis://39.108.248.8:6379

        // 构造redisson实现分布式锁必要的Config
        Config config = new Config();
        config.useSingleServer().setAddress("redis://39.108.248.8:6379").setDatabase(0);
        // 构造RedissonClient
        RedissonClient redisson = create(config);

        RLock rLock=redisson.getLock(lockKey);  //获取锁对象
        try{
            rLock.tryLock(30,TimeUnit.SECONDS);  //加一个30秒超时的锁
            int stock=Integer.parseInt(redisTemplate.opsForValue().get("stockE"));  //jedis.get("stock")

            if(stock > 0){
                int realStock=stock-1;
                redisTemplate.opsForValue().set("stockE",realStock+"");
                System.out.println("扣减成功，剩余库存："+realStock);
                return "扣减成功，剩余库存："+realStock;
            }else{
                System.out.println("扣减失败，库存不足");
                return "扣减失败，库存不足";
            }
        }finally {
            rLock.unlock();  //释放锁
        }
    }


    /**
     * 只删除同一线程的锁
     * @return
     */
    @RequestMapping("/deduct_stockD")
    public String deductStockD(){
        String lockKey="lockKey";
        String clientId= UUID.randomUUID().toString();
        try{
            Boolean checkLock=redisTemplate.opsForValue().setIfAbsent(lockKey,"lock",10, TimeUnit.SECONDS);  //设置10秒后自动失效
            if(!checkLock){
                return "系统繁忙，请稍后再试";
            }
            int stock=Integer.parseInt(redisTemplate.opsForValue().get("stockD"));  //jedis.get("stock")

            if(stock > 0){
                int realStock=stock-1;
                redisTemplate.opsForValue().set("stockD",realStock+"");
                System.out.println("扣减成功，剩余库存："+realStock);
                return "扣减成功，剩余库存："+realStock;
            }else{
                System.out.println("扣减失败，库存不足");
                return "扣减失败，库存不足";
            }
        }finally {
            if(clientId.equals(redisTemplate.opsForValue().get(lockKey))){
                redisTemplate.delete(lockKey);
            }
        }
    }

    @RequestMapping("/deduct_stockC")
    public String deductStockC(){
        String lockKey="lockKey";
        try{
            Boolean checkLock=redisTemplate.opsForValue().setIfAbsent(lockKey,"lock",10, TimeUnit.SECONDS);
            if(!checkLock){
                return "系统繁忙，请稍后再试";
            }
            int stock=Integer.parseInt(redisTemplate.opsForValue().get("stockC"));  //jedis.get("stock")

            if(stock > 0){
                int realStock=stock-1;
                redisTemplate.opsForValue().set("stockC",realStock+"");
                System.out.println("扣减成功，剩余库存："+realStock);
                return "扣减成功，剩余库存："+realStock;
            }else{
                System.out.println("扣减失败，库存不足");
                return "扣减失败，库存不足";
            }
        }finally {
            redisTemplate.delete(lockKey);
        }
    }

    @RequestMapping("/deduct_stockB")
    public String deductStockB(){
        synchronized (this){
            int stock=Integer.parseInt(redisTemplate.opsForValue().get("stockB"));  //jedis.get("stock")

            if(stock > 0){
                int realStock=stock-1;
                redisTemplate.opsForValue().set("stockB",realStock+"");
                System.out.println("扣减成功，剩余库存："+realStock);
                return "扣减成功，剩余库存："+realStock;
            }else{
                System.out.println("扣减失败，库存不足");
                return "扣减失败，库存不足";
            }
        }
    }

    @RequestMapping("/deduct_stockA")
    public String deductStockA(){
        int stock=Integer.parseInt(redisTemplate.opsForValue().get("stockA"));  //jedis.get("stock")

        if(stock > 0){
            int realStock=stock-1;
            redisTemplate.opsForValue().set("stockA",realStock+"");
            System.out.println("扣减成功，剩余库存："+realStock);
            return "扣减成功，剩余库存："+realStock;
        }else{
            System.out.println("扣减失败，库存不足");
            return "扣减失败，库存不足";
        }
    }
}
