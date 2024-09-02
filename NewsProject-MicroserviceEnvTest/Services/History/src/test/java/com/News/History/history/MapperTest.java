package com.News.History.history;

import com.News.History.kafka.UserHistoryDTO;
import com.News.History.websocket.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MapperTest {

    private Mapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new Mapper();
    }

    @Test
    void shouldMapToUserHistory(){
        UserHistoryDTO dto = new UserHistoryDTO();
        dto.setKey_cloak_id("12345");
        dto.setAuthor("John Doe");
        dto.setTitle("Sample Title");
        dto.setDescription("Sample Description");
        dto.setUrl("http://example.com");
        dto.setUrlToImage("http://example.com/image.jpg");
        dto.setPublishedAt("2024-09-02T10:00:00Z");
        dto.setContent("Sample Content");

        UserHistoryDTO.Source source = new UserHistoryDTO.Source("sourceId","sourceName");
        dto.setSource(source);

        // Act: call the method to test
        UserHistory userHistory = mapper.toUserHistory(dto);

        // Assert: check that the returned UserHistory matches the input UserHistoryDTO
        assertEquals(dto.getKey_cloak_id(), userHistory.getKey_cloak_id());
        assertEquals(dto.getSource().getId(), userHistory.getSourceID());
        assertEquals(dto.getSource().getName(), userHistory.getSourceName());
        assertEquals(dto.getAuthor(), userHistory.getAuthor());
        assertEquals(dto.getTitle(), userHistory.getTitle());
        assertEquals(dto.getDescription(), userHistory.getDescription());
        assertEquals(dto.getUrl(), userHistory.getUrl());
        assertEquals(dto.getUrlToImage(), userHistory.getUrlToImage());
        assertEquals(dto.getPublishedAt(), userHistory.getPublishedAt());
        assertEquals(dto.getContent(), userHistory.getContent());
    }

    @Test
    public void testToArticle() {
        // Arrange
        UserHistory userHistory = UserHistory.builder()
                .key_cloak_id("keycloak123")
                .sourceID("sourceId")
                .sourceName("sourceName")
                .author("authorName")
                .title("articleTitle")
                .description("description")
                .url("https://example.com")
                .urlToImage("https://example.com/image.jpg")
                .publishedAt("2024-09-02T12:00:00Z")
                .content("content")
                .build();

        // Act
        Article article = mapper.toArticle(userHistory);

        // Assert
        assertThat(article.getSource().getId()).isEqualTo("sourceId");
        assertThat(article.getSource().getName()).isEqualTo("sourceName");
        assertThat(article.getAuthor()).isEqualTo("authorName");
        assertThat(article.getTitle()).isEqualTo("articleTitle");
        assertThat(article.getDescription()).isEqualTo("description");
        assertThat(article.getUrl()).isEqualTo("https://example.com");
        assertThat(article.getUrlToImage()).isEqualTo("https://example.com/image.jpg");
        assertThat(article.getPublishedAt()).isEqualTo("2024-09-02T12:00:00Z");
        assertThat(article.getContent()).isEqualTo("content");
    }

}