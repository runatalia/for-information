package com.example.userservice.config;


import com.example.userservice.dto.response.KafkaUpdateEmailUserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class KafkaConfig {
    public static final String DATABASE_USER_CHANGE = "DATABASE_USER_CHANGE";
    public static final String TRANSACTION_STATUS = "TRANSACTION_STATUS";
    public static final String KEY_ID_UPDATE_EMAIL = "UPDATE_EMAIL";
    public static final String KEY_ID_DELETE_USER = "DELETE_USER";
    public static final String TRANSACTION_STATUS_UPDATE_EMAIL = "TRANSACTION_STATUS_UPDATE_EMAIL";
    public static final String TRANSACTION_STATUS_DELETE_USER = "TRANSACTION_STATUS_DELETE_USER";

    public NewTopic topic() {
        return TopicBuilder
                .name(DATABASE_USER_CHANGE)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public ConsumerFactory<String, KafkaUpdateEmailUserResponse> consumerFactory(KafkaProperties kafkaProperties, ObjectMapper mapper) {
        var configProps = kafkaProperties.buildConsumerProperties();
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        var kafkaConsumerFactory = new DefaultKafkaConsumerFactory<String, KafkaUpdateEmailUserResponse>(configProps);
        kafkaConsumerFactory.setValueDeserializer(new JsonDeserializer<>(mapper));
        return kafkaConsumerFactory;
    }

    @Bean("listenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, KafkaUpdateEmailUserResponse>>
    listenerContainerFactory(ConsumerFactory<String, KafkaUpdateEmailUserResponse> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, KafkaUpdateEmailUserResponse>();
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setConcurrency(1);
        factory.getContainerProperties().setIdleBetweenPolls(1_000);
        factory.getContainerProperties().setPollTimeout(1_000);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        var executor = new SimpleAsyncTaskExecutor("k-consumer-");
        executor.setConcurrencyLimit(10);
        var listenerTaskExecutor = new ConcurrentTaskExecutor(executor);
        factory.getContainerProperties().setListenerTaskExecutor(listenerTaskExecutor);
        return factory;
    }
}
