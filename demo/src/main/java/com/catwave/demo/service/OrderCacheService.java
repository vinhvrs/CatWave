package com.catwave.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderCacheService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void saveOrder(String orderId, String orderInfo) {
        // Set with TTL, e.g. 15 mins
        redisTemplate.opsForValue().set(orderId, orderInfo, 15, java.util.concurrent.TimeUnit.MINUTES);
    }

    public String getOrder(String orderId) {
        return redisTemplate.opsForValue().get(orderId);
    }

    public void removeOrder(String orderId) {
        redisTemplate.delete(orderId);
    }
}
