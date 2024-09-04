package com.News.NewsAPI.kafka;

import com.News.NewsAPI.news.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DTOMapperTest {

    private DTOMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DTOMapper();
    }

    @Test
    void shouldMap_toUserData(){

        UserHistoryDTO DTO = mapper.toUserDto(
                Article.builder()
                        .source(new Article.Source("sourceId","name"))
                        .author("newAuthor")
                        .title("newTitle")
                        .build(),"newId"
        );

        assertEquals("newAuthor", DTO.getAuthor());
        assertEquals("newTitle", DTO.getTitle());
    }

}