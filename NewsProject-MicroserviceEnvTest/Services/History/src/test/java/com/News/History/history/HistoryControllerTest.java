package com.News.History.history;

import com.News.History.analytics.AnalyticService;
import com.News.History.websocket.WebsocketService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = HistoryController.class)
class HistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoryService historyService;

    @MockBean
    private WebsocketService websocketService;

    @MockBean
    private AnalyticService analyticService;

    @Test
    void getAllHistories() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/history"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void shouldCallHistory() throws Exception {

        String userId = "anyId";

        mockMvc.perform(MockMvcRequestBuilders.get("/history/get")
                .header("User-ID", userId)).andExpect(MockMvcResultMatchers.status().isOk());

        verify(websocketService,times(1)).getAllNews(userId);

    }

    @Test
    void shouldSend() throws Exception {

        when(analyticService.sendAllSourceNames()).thenReturn(Mono.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/history/pref"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

}