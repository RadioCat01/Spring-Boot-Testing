package com.Ecom.Customer.customer;

import lombok.Builder;

public record CustomerResponse(
        String Id,
        String firstname,
        String lastname,
        String email,
        Address address
) {
}
