package com.Ecom.Order.orderline;

import com.Ecom.Order.order.OrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderLineServiceTest {

    @Autowired
    private OrderLineRepository orderLineRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderLineService orderLineService;

    @Test
    void shouldReturnIntegerWhenSaveOrderLine() {
        OrderLineRequest request = new OrderLineRequest(1,2,3,4);

        Integer id = orderLineService.saveOrderLine(request);

        assertNotNull(id);
    }
    @Test
    void shouldReturnListOfOrderLineResponses(){
        List<OrderLineResponse> responses = orderLineService.findAllByOrderId(1);
        assertNotNull(responses);

    }

}