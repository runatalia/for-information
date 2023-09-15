package com.example.userservice.service.kafka;


import com.example.userservice.dto.request.UserEmailDto;
import com.example.userservice.dto.response.KafkaDeleteUserResponse;
import com.example.userservice.dto.response.KafkaUpdateEmailUserResponse;
import com.example.userservice.enums.StatusTransaction;
import com.example.userservice.service.CandidateService;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import static com.example.userservice.config.KafkaConfig.*;


@RequiredArgsConstructor
@Slf4j
@Service
public class Consumer {

    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final CandidateService candidateService;
    private final java.util.logging.Logger logInFile = java.util.logging.Logger.getLogger("TransactionLog");
    private static FileHandler filehandler;

    {
        try {
            filehandler = new FileHandler("TransactionLog.log");
        } catch (IOException e) {
            log.error("Error in logHandlerToFile. Can't write in file");
        }
    }

    @RetryableTopic(attempts = "5",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            exclude = {ListenerExecutionFailedException.class, NotFoundException.class, NullPointerException.class}, traversingCauses = "true",
            dltStrategy = DltStrategy.NO_DLT,
            backoff = @Backoff(delay = 1000, multiplier = 2, maxDelay = 5000))
    @Transactional
    @KafkaListener(topics = TRANSACTION_STATUS)
    public void consume(final @Payload String message,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_KEY) String key,
                        final @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
    ) {
        log.info(String.format("#### -> Consumed message -> TIMESTAMP: %d\n%s\noffset: %d\nkey: %s\npartition: %d\ntopic: %s", ts, message, offset, key, partition, topic));
        try {
            switch (key) {
                case TRANSACTION_STATUS_UPDATE_EMAIL:
                    var kafkaUpdateUserResponseDto = objectMapper.readValue(message, KafkaUpdateEmailUserResponse.class);
                    if (kafkaUpdateUserResponseDto.getStatusTransaction().equals(StatusTransaction.FAIL)) {
                        var userUpdateEmail = new UserEmailDto(kafkaUpdateUserResponseDto.getNewEmail(), kafkaUpdateUserResponseDto.getEmail());
                        log.warn(String.format("#### -> Consumed message -> rollback attempt: TIMESTAMP: %d\n%s\noffset: %d\nkey: %s\npartition: %d\ntopic: %s", ts, message, offset, key, partition, topic));
                        userService.updateEmail(userUpdateEmail);
                        candidateService.updateEmail(userUpdateEmail);
                        break;
                    }
                case TRANSACTION_STATUS_DELETE_USER:
                    var kafkaDeleteUserResponseDto = objectMapper.readValue(message, KafkaDeleteUserResponse.class);
                    if (kafkaDeleteUserResponseDto.getStatusTransaction().equals(StatusTransaction.FAIL)) {
                        log.warn(String.format("#### -> Consumed message -> rollback attempt: TIMESTAMP: %d\n%s\noffset:" +
                                " %d\nkey: %s\npartition: %d\ntopic: %s", ts, message, offset, key, partition, topic));
                        userService.setUserEnabled(kafkaDeleteUserResponseDto.getEmail(), true);
                        candidateService.setCandidateIsBlocked(kafkaDeleteUserResponseDto.getEmail(), false);
                    } else {
                        log.warn(String.format("#### -> Consumed message -> successfully: TIMESTAMP: %d\n%s\noffset:" +
                                " %d\nkey: %s\npartition: %d\ntopic: %s", ts, message, offset, key, partition, topic));
                        userService.deleteUser(kafkaDeleteUserResponseDto.getEmail());
                        candidateService.deleteByEmail(kafkaDeleteUserResponseDto.getEmail());
                    }
                    break;
            }
        } catch (JsonProcessingException ex) {
            log.warn("can't parse message:{}", message, ex);
        } catch (Exception ex) {
            log.error("transaction error. Check the log file");
            logHandlerToFile(String.format("#### -> Transaction error. User-service consumed message -> TIMESTAMP:" +
                    " %d\n%s\noffset: %d\nkey: %s\npartition: %d\ntopic: %s", ts, message, offset, key, partition, topic));
        }
        log.info(String.format("#### -> Consumed message finished processing the message -> TIMESTAMP: %d\n%s\noffset: %d\nkey: %s\npartition: %d\ntopic: %s", ts, message, offset, key, partition, topic));
    }

    private void logHandlerToFile(String message) {
        logInFile.addHandler(filehandler);
        SimpleFormatter formatter = new SimpleFormatter();
        filehandler.setFormatter(formatter);
        logInFile.log(Level.WARNING, message);
    }
}