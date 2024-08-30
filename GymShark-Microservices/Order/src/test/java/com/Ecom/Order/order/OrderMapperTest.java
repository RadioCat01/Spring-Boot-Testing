package com.Ecom.Order.order;

import com.Ecom.Order.product.PurchaseRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OrderMapperTest {

    OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        orderMapper = new OrderMapper();
    }

    @Test
    void shouldMap_toOrder(){
        List<PurchaseRequest> purchaseRequests = new ArrayList<>();
        purchaseRequests.add(new PurchaseRequest(1,10));
        OrderRequest request = new OrderRequest(
                1,
                "ref",
                new BigDecimal(10),
                PaymentMethod.PAYPAL,
                purchaseRequests,
                "new house number",
                "houseNum",
                "11880"
        );

        Order order =  orderMapper.toOrder(request);

        assertNotNull(order);
        assertEquals(order.getId(),request.id());
        assertEquals(order.getReference(),request.reference());
        assertEquals(order.getPaymentMethod(),request.paymentMethod());
    }

    @Test
    void shouldMap_toOrderResponse(){
        Order order = Order.builder()
                .id(1)
                .reference("ref")
                .paymentMethod(PaymentMethod.PAYPAL)
                .customerId("anyId")
                .build();

        OrderResponse response = orderMapper.fromOrder(order);

        assertNotNull(response);
        assertEquals(response.id(),1);
        assertEquals(response.reference(),"ref");
        assertEquals(response.paymentMethod(),PaymentMethod.PAYPAL);
    }

}