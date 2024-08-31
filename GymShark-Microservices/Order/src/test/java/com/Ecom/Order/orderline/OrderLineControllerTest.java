package com.Ecom.Order.orderline;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = OrderLineController.class)
class OrderLineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderLineService orderLineService;

    @Test
    void shouldFindByOrderId() throws Exception {

        when(orderLineService.findAllByOrderId(anyInt())).thenReturn(any());

        mockMvc.perform(MockMvcRequestBuilders.get("/ap/v1/order-lines/order/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

}