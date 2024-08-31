package com.Ecom.Order.kafka;

import com.Ecom.Order.customer.CustomerResponse;
import com.Ecom.Order.order.PaymentMethod;
import com.Ecom.Order.product.PurchaseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class OrderProducerTest {

    @Mock
    private KafkaTemplate<String, OrderConfirmation> kafkaTemplate;

    @InjectMocks
    private OrderProducer orderProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSendOrderConfirmation() {
        //Given
        OrderConfirmation confirmation = new OrderConfirmation(
                "ref",new BigDecimal(100), PaymentMethod.PAYPAL,
                new CustomerResponse("anyId","anyName","lastName","Email"),
                List.of(new PurchaseResponse(1,"name","des",new BigDecimal(10),10))
        );
        ArgumentCaptor<Message<OrderConfirmation>> captor = ArgumentCaptor.forClass(Message.class);
        orderProducer.sendOrderConfirmation(confirmation);

        verify(kafkaTemplate).send(captor.capture());
    }

}