package com.Ecom.Payment.notification;

import com.Ecom.Payment.payment.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class NotificationProducerTest {

    @Mock
    private KafkaTemplate<String, PaymentNotificationRequest> kafkaTemplate;

    @InjectMocks
    private NotificationProducer notificationProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSendNotification() {

        PaymentNotificationRequest request = new PaymentNotificationRequest(
                "ref", new BigDecimal(10), PaymentMethod.PAYPAL,
                "firstName", "LastName","Email"
        );

        ArgumentCaptor<Message<PaymentNotificationRequest>> captor = ArgumentCaptor.forClass(Message.class);
        notificationProducer.sendNotification(request);

        verify(kafkaTemplate, times(1)).send(captor.capture());


    }

}