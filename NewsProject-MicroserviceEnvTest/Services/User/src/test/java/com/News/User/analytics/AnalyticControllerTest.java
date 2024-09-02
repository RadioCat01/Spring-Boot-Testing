package com.News.User.analytics;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = AnalyticController.class)
class AnalyticControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldReceiveAnalytics() throws Exception {

    }
}