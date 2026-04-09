package com.example.demo;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class Controller {
    private final AtomicInteger counter = new AtomicInteger(0);

    @Value("${server.port:8080}")
    private int port;
    private final StringRedisTemplate redisTemplate;
    private static final String COUNTER_KEY = "global_counter";

    public Controller(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping(value = "/dot", produces = "application/json")
    public String index() {
        int value = counter.incrementAndGet();
        return String.format("{\"counter\":%d,\"port\":%d}", value, port);
    }
    @GetMapping(value = "/", produces = "application/json")
    public String indexredis() {
        Long value = redisTemplate.opsForValue().increment(COUNTER_KEY);
        return String.format("{\"counter\":%d,\"port\":%d}", value, port);
    }
}
