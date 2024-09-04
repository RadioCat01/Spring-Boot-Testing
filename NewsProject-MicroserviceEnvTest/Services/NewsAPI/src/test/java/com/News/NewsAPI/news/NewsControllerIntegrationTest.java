package com.News.NewsAPI.news;

import com.News.NewsAPI.kafka.DTOMapper;
import com.News.NewsAPI.kafka.Producer;
import com.News.NewsAPI.kafka.UserHistoryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebFluxTest
class NewsControllerIntegrationTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private NewsService newsService;

    @MockBean
    private Producer producer;

    @MockBean
    private DTOMapper dtoMapper;

    @Test
    void shouldGetNews(){

        when(newsService.getNews(anyInt(),anyString())).thenReturn(Flux.just(new Article(), new Article()));

        webClient.get().uri("/news?pageSize=5")
                .header("User-ID","anyId")
                .exchange()   // triggers the execution of HTTP request + retrieve response + Support for assertions
                .expectStatus().isOk()
                .expectBodyList(Article.class)
                .hasSize(2)
                .contains(); //check what's in the list

    }

    @Test
    void shouldGetSearch(){

        Article article = Article.builder()
                .author("anyAuthor")
                .title("anyTitle")
                .description("anyDescription")
                .url("http://anyUrl")
                .build();

        when(newsService.search(anyInt(),anyString())).thenReturn(Flux.just(article));

        webClient.get().uri(uriBuilder -> uriBuilder
                        .path("/news/search")
                        .queryParam("pageSize",5)
                        .queryParam("keyword","technology")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Article.class)
                .hasSize(1);

    }

    @Test
    void shouldSendNews() throws Exception {

        Article article = Article.builder()
                .author("anyAuthor")
                .title("anyTitle")
                .description("anyDescription")
                .url("http://anyUrl")
                .build();

        UserHistoryDTO message = UserHistoryDTO.builder()
                .author("antAuthor")
                .title("anyTitle")
                .description("anyDescription")
                .url("http://anyUrl")
                .build();

        when(dtoMapper.toUserDto(any(Article.class),anyString())).thenReturn(message);

        webClient.post().uri("/news/kafka")
                .header("User-ID","anyId")
                .bodyValue(article)
                .exchange()
                .expectStatus().isOk();

        verify(dtoMapper,times(1)).toUserDto(any(Article.class),anyString());
        verify(producer,times(1)).sendMessage(any(UserHistoryDTO.class));

    }

    @Test
    void shouldGetPref(){

        webClient.get().uri(uriBuilder -> uriBuilder
                .path("/news/pref")
                .queryParam("id","anyid")
                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class);

    }

}