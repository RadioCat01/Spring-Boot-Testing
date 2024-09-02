package com.News.History.kafka;

import com.News.History.history.HistoryService;
import com.News.History.history.UserHistory;
import com.News.History.websocket.WebsocketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static reactor.core.publisher.Mono.when;

class ConsumerTest {

    @Mock
    private HistoryService historyService;

    @Mock
    private WebsocketService websocketService;

    @InjectMocks
    private Consumer consumer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void shouldConsumeMessageAndSaveSuccessfully() {

    }

}