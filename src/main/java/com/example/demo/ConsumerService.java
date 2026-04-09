package com.example.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Service
public class ConsumerService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String INPUT_QUEUE = "input_queue";
    private static final String OUTPUT_QUEUE = "output_queue";

    public ConsumerService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void startProcessing() {
        CompletableFuture.runAsync(() -> {
            while (true) {
                // Ждем сообщение из входной очереди
                String task = redisTemplate.opsForList().rightPop(INPUT_QUEUE, Duration.ofSeconds(10));
                System.out.println("Consumer принял: " + task);
                if (task != null) {
                    String processedData = task.toUpperCase() + "_PROCESSED";
                    // Отправляем в выходную очередь
                    redisTemplate.opsForList().leftPush(OUTPUT_QUEUE, processedData);
                }
            }
        });
    }
}