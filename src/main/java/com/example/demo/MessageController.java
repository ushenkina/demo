package com.example.demo;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Objects;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String OUTPUT_QUEUE = "output_queue";

    public MessageController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getProcessedMessages() {
        return Flux.interval(Duration.ofMillis(2000))
                .map(tick -> redisTemplate.opsForList().rightPop(OUTPUT_QUEUE))
                .filter(Objects::nonNull);
    }
}
