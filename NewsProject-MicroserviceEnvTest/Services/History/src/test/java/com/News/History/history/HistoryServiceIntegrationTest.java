package com.News.History.history;

import com.News.History.kafka.UserHistoryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class HistoryServiceIntegrationTest {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private Mapper mapper;

    @Test
    void shouldSaveHistory() {
        UserHistoryDTO newHistoryDTO = UserHistoryDTO.builder()
                .key_cloak_id("anyId")
                .source(new UserHistoryDTO.Source("newId","newName"))
                .author("newAuthor")
                .title("duplicateTitle")
                .build();

        Mono<UserHistory> userHistoryMono = historyService.save(newHistoryDTO);

        assertNotNull(userHistoryMono);
    }

    @Test
    void shouldThrowIllegalStateException(){
        UserHistoryDTO newHistoryDTO = UserHistoryDTO.builder()
                .key_cloak_id("anyId")
                .source(new UserHistoryDTO.Source("newId", "newName"))
                .author("newAuthor")
                .title("duplicateTitle")
                .build();
        Mono<UserHistory> userHistoryMono = historyService.save(newHistoryDTO);

        UserHistoryDTO newHistoryDTO2 = UserHistoryDTO.builder()
                .key_cloak_id("anyId")
                .source(new UserHistoryDTO.Source("newId", "newName"))
                .author("newAuthor")
                .title("duplicateTitle")
                .build();

        Mono<UserHistory> userHistoryMono2 = historyService.save(newHistoryDTO);


        StepVerifier.create(userHistoryMono2)
                .expectErrorMatches(throwable -> throwable instanceof IllegalStateException &&
                        throwable.getMessage().equals("Record with this title already exists"))
                .verify();  //Reactive check
    }

    @Test
    void shouldFindAllHistory() {
        Flux<UserHistory> list = historyService.findall();

        assertNotNull(list);
    }

}