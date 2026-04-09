package com.example.demo;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class ProducerService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String INPUT_QUEUE = "input_queue";
    private final Random random = new Random();

    public ProducerService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(fixedDelayString = "#{T(java.util.concurrent.ThreadLocalRandom).current().nextInt(2000, 5000)}")
    public void generateMessage() {
        String messageId = UUID.randomUUID().toString();
        String payload = "Msg-" + messageId;

        // Кладём во входную очередь
        redisTemplate.opsForList().leftPush(INPUT_QUEUE, payload);
        System.out.println("Producer отправил: " + payload);
    }
}
