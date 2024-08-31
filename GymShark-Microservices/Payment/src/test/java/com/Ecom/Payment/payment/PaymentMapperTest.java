package com.Ecom.Payment.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class PaymentMapperTest {

    private PaymentMapper paymentMapper;

    @BeforeEach
    void setUp() {
        paymentMapper = new PaymentMapper();
    }
    @Test
    void shouldMap_toPayment() {
        PaymentRequest request = new PaymentRequest(
                1, new BigDecimal(10),PaymentMethod.PAYPAL,
                2,"ref",new CustomerResponse(
                      "id","fName","lName","mail")
        );

        Payment payment = paymentMapper.toPayment(request);

        assertEquals(payment.getId(),request.id());
        assertEquals(payment.getPaymentMethod(),request.paymentMethod());
    }

}