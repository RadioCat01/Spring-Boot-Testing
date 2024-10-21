package com.ReelsOrbit.userService.payment;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(name = "PaymentService", url = "")
public interface paymentClient {

    @PostMapping
    Response requestPayment(@RequestBody PaymentRequest request);
}
