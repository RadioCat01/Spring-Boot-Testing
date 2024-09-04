package com.News.NewsAPI.news;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NewsServiceTest {

    @Autowired
    private NewsService newsService;

    @Test
    void shouldThrowExceptionWhenGetUpdates() {

        Flux<Article> articles = newsService.getUpdates();

        StepVerifier.create(articles)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().contains("Failed to fetch updates"))
                .verify(); //Have to fail manually
    }

    @Test
    void shouldGetNews(){
        StepVerifier.create(newsService.getNews(1,"1")).expectNextCount(1).verifyComplete();
    }

    @Test
    void shouldThrowExceptionWhenGetNews() {
      StepVerifier.create(newsService.getNews(1, "Wrong-Id"))
              .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                      throwable.getMessage().contains("An error occurred while fetching news"))
              .verify();

      //have to fail manually
    }

    @Test
    void fallbackShouldReturnDefaultArticle() {

        String expectedTitle = "Hey there We're having a problem right now";
        Throwable simulatedThrowable = new RuntimeException("Simulated exception");


        Flux<Article> result = newsService.fallback(4, "testId", simulatedThrowable);


        StepVerifier.create(result)
                .expectNextMatches(article -> article.getTitle().equals(expectedTitle))
                .verifyComplete();
    }

    @Test
    void shouldThrowErrorWhenGetPreferences(){
        StepVerifier.create(newsService.getPreferences("1")).expectErrorMatches(throwable -> throwable instanceof RuntimeException
        && throwable.getMessage().contains("An error occurred while fetching news")).verify();
    }

    @Test
    void shouldGetUpdates() {
        Flux<Article> articles = newsService.getUpdates();
        articles.subscribe();

        assertNotNull(articles);
    }
}