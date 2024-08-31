package com.Ecom.Order.order;

import com.Ecom.Order.customer.CustomerClient;
import com.Ecom.Order.customer.CustomerResponse;
import com.Ecom.Order.kafka.OrderConfirmation;
import com.Ecom.Order.kafka.OrderProducer;
import com.Ecom.Order.orderline.OrderLine;
import com.Ecom.Order.orderline.OrderLineRequest;
import com.Ecom.Order.orderline.OrderLineService;
import com.Ecom.Order.payment.PaymentClient;
import com.Ecom.Order.payment.PaymentRequest;
import com.Ecom.Order.product.ProductClient;
import com.Ecom.Order.product.ProductClientFeing;
import com.Ecom.Order.product.PurchaseRequest;
import com.Ecom.Order.product.PurchaseResponse;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class OrderServiceUnitTest {

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerClient customerClient;

    @Mock
    private ProductClient productClient;

    @Mock
    private OrderLineService orderLineService;

    @Mock
    private ProductClientFeing productClientFeing;

    @Mock
    private OrderProducer orderProducer;

    @Mock
    private PaymentClient paymentClient;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    //Skipped dut to advanced mocking requirement in open feing


}