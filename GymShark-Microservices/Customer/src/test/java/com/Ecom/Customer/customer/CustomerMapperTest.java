package com.Ecom.Customer.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    public CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        customerMapper = new CustomerMapper();
    }

    @Test
    void shouldMapToCustomer(){
        //GIven
        CustomerRequest customerRequest = CustomerRequest.builder()
                .Id("any id")
                .firstname("any Name")
                .lastname("any Lastname")
                .email("any email")
                .build();

        Customer customer = customerMapper.toCustomer(customerRequest);

        assertEquals(customer.getId(), "any id");
        assertEquals(customer.getFirstname(), "any Name");
        assertEquals(customer.getLastname(), "any Lastname");
        assertEquals(customer.getEmail(), "any email");
        System.out.print("Successfully Mapped");
    }

    @Test
    void shouldConvertCustomerToCustomerResponse() {
        // Given
        Customer customer = new Customer();

        customer.setFirstname("John");
        customer.setLastname("Doe");
        customer.setEmail("john.doe@example.com");

        CustomerMapper customerMapper = new CustomerMapper();

        // When
        CustomerResponse response = customerMapper.fromCustomer(customer);

        // Then
        assertEquals(customer.getFirstname(), response.firstname());
        assertEquals(customer.getLastname(), response.lastname());
        assertEquals(customer.getEmail(), response.email());
        System.out.print("Successfully Mapped");
    }
}