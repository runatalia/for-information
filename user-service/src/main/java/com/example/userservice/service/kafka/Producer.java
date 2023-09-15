package com.example.userservice.service.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.example.userservice.config.KafkaConfig.DATABASE_USER_CHANGE;


@RequiredArgsConstructor
@Slf4j
@Service
public class Producer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public <T> CompletableFuture<SendResult<String, String>> sendMessageUserChange(String topic, String key, T message) {
        String messageAsString = null;
        try {
            messageAsString = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException ex) {
            log.warn("Can't serialize json:{}", message.toString(), ex);
        }
        log.info(String.format("#### -> Produsered message -> \nkey: %s\nmessege: %s\ntopic: %s",
                key, messageAsString, topic));
        return this.kafkaTemplate.send(DATABASE_USER_CHANGE, key, messageAsString);
    }
}