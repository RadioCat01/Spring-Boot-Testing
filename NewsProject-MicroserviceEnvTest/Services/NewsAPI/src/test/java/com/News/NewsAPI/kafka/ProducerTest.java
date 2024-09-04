package com.News.NewsAPI.kafka;

import org.apache.kafka.common.protocol.types.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static org.junit.jupiter.api.Assertions.*;

class ProducerTest {

    @Mock
    private KafkaTemplate<String,UserHistoryDTO> kafkaTemplate;

    @InjectMocks
    private Producer producer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void producer() {
        UserHistoryDTO dto = new UserHistoryDTO();

        producer.sendMessage(dto);

        Message<UserHistoryDTO> expectedMessage = MessageBuilder.withPayload(dto)
                .setHeader(KafkaHeaders.TOPIC, "news").build();
    }


}