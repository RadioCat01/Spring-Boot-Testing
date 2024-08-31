package com.Ecom.Order.orderline;

import com.Ecom.Order.order.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderLineMapperTest {

    private OrderLineMapper orderLineMapper;

    @BeforeEach
    void setUp() {
        orderLineMapper = new OrderLineMapper();
    }

    @Test
    void shouldMap_toOrderLine() {
        OrderLineRequest request = new OrderLineRequest(1,2,3,4);

        OrderLine orderLine = orderLineMapper.toOrderLine(request);

        assertNotNull(orderLine);
        assertEquals(orderLine.getId(),request.id());
        assertEquals(orderLine.getQuantity(),request.quantity());
        assertEquals(orderLine.getProductId(),request.productId());
    }

    @Test
    void shouldMap_fromOrderLine() {
        OrderLine orderLine = OrderLine.builder()
                .id(1)
                .quantity(2)
                .productId(3)
                .order(Order.builder()
                        .id(2)
                        .customerId("any")
                        .build())
                .build();

        OrderLineResponse response = orderLineMapper.toOrderLineResponse(orderLine);

        assertNotNull(response);
        assertEquals(orderLine.getId(),response.id());
        assertEquals(orderLine.getQuantity(),response.quantity());

    }

}