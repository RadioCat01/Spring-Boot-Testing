package com.News.History.websocket;

import com.News.History.history.HistoryRepository;
import com.News.History.history.Mapper;
import com.News.History.history.UserHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;


class WebsocketServiceUnitTest {

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private NewsWebSocketHandler newsWebSocketHandler;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private WebsocketService websocketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSendAllNews(){
        when(historyRepository.findAll()).thenReturn(Flux.just(UserHistory.builder().build()));
        when(mapper.toArticle(any(UserHistory.class))).thenReturn(new Article());

        StepVerifier.create(websocketService.sendAllNews()).verifyComplete();

        verify(historyRepository,times(1)).findAll();
        verify(mapper,times(1)).toArticle(any(UserHistory.class));
        verify(newsWebSocketHandler,times(1)).broadcastNews(any());
    }

    @Test
    void shouldGetNews(){
        when(historyRepository.findAll()).thenReturn(Flux.just(UserHistory.builder().build()));
        when(mapper.toArticle(any(UserHistory.class))).thenReturn(new Article());

        websocketService.getAllNews();

        verify(historyRepository,times(1)).findAll();
        verify(mapper,times(1)).toArticle(any(UserHistory.class));
        verify(newsWebSocketHandler,times(1)).broadcastNews(any());

    }

    @Test
    void shouldWatchNews(){
        UserHistory userHistory1 = new UserHistory();
        UserHistory userHistory2 = new UserHistory();

        when(historyRepository.findAll()).thenReturn(Flux.just(userHistory1,userHistory2));

        StepVerifier.create(websocketService.watchNews())          //From Reactor Testing Framework
                .expectNext(userHistory1)
                .expectNext(userHistory2)
                .verifyComplete();

        verify(historyRepository,times(1)).findAll();
    }

}