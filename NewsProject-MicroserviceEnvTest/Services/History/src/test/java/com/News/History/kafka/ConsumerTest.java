package com.News.History.kafka;

import com.News.History.history.HistoryRepository;
import com.News.History.history.HistoryService;
import com.News.History.history.UserHistory;
import com.News.History.websocket.WebsocketService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static reactor.core.publisher.Mono.when;


@SpringBootTest
class ConsumerTest {

    @Autowired
    HistoryService historyService;

    @Autowired
    Consumer consumer;

    @Autowired
    private WebsocketService websocketService;

    @Test
    void shouldConsumeHistory() {
        UserHistoryDTO request = UserHistoryDTO.builder()
                .key_cloak_id("anyId2")
                .source(new UserHistoryDTO.Source("anyId","anyName"))
                .author("anyAuthor")
                .title("anyTitle")
                .description("anyDescription")
                .content("anyContent")
                .publishedAt("anyPublishedAt")
                .url("any")
                .urlToImage("anyUrlToImage")
                .build();

       Disposable message = consumer.consumeMessage(request);

       assertNotNull(message);

    }

}