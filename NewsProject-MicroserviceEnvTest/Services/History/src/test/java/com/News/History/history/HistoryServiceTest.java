package com.News.History.history;

import com.News.History.kafka.UserHistoryDTO;
import org.apache.catalina.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HistoryServiceTest {

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private HistoryService historyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveHistory() {
        UserHistoryDTO newHistoryDTO = UserHistoryDTO.builder()
                .key_cloak_id("anyId")
                .source(new UserHistoryDTO.Source("newId","newName"))
                .author("newAuthor")
                .title("newTitle")
                .build();

        UserHistory history = UserHistory.builder()
                .id(null)
                .key_cloak_id("anyId")
                .title("anyTitle")
                .author("newAuthor")
                .build();

        when(historyRepository.existsByTitle(newHistoryDTO.getTitle())).thenReturn(Mono.just(false));
        when(mapper.toUserHistory(newHistoryDTO)).thenReturn(history);
        when(historyRepository.save(history)).thenReturn(Mono.just(history));

        Mono<UserHistory> historyMono = historyService.save(newHistoryDTO);
        historyMono.subscribe(Assertions::assertNotNull);
        /* need to be subscribed as this is a Reactive Pipeline
           Otherwise the verifications won't be picked up */

        assertNotNull(historyMono);

        verify(historyRepository,times(1)).existsByTitle(anyString());
        verify(mapper,times(1)).toUserHistory(newHistoryDTO);
        verify(historyRepository,times(1)).save(history);
    }

    @Test
    void shouldThrowException(){
        UserHistoryDTO newHistoryDTO = UserHistoryDTO.builder()
                .key_cloak_id("anyId")
                .source(new UserHistoryDTO.Source("newId", "newName"))
                .author("newAuthor")
                .title("duplicateTitle") // Title to simulate the duplicate
                .build();

        when(historyRepository.existsByTitle(newHistoryDTO.getTitle())).thenReturn(Mono.just(true));

        Mono<UserHistory> historyMono= historyService.save(newHistoryDTO);

        StepVerifier.create(historyMono)
                .expectErrorMatches(throwable -> throwable instanceof IllegalStateException &&
                        throwable.getMessage().equals("Record with this title already exists"))
                .verify();

    }

    @Test
    void shouldFindHistory() {
        Flux<UserHistory> historyFlux = Flux.just(UserHistory.builder().key_cloak_id("1").title("a").build());
        when(historyRepository.findAll()).thenReturn(historyFlux);

        Flux<UserHistory> userHistoryFlux = historyService.findall();

        userHistoryFlux.subscribe(Assertions::assertNotNull);

        assertNotNull(userHistoryFlux);
        verify(historyRepository,times(1)).findAll();
    }

}