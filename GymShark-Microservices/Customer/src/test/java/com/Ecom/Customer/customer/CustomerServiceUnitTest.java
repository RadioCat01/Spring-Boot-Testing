package com.Ecom.Customer.customer;

import com.Ecom.Customer.exception.CustomerNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CustomerServiceUnitTest {

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveCustomer(){
        //Given
        CustomerRequest request = CustomerRequest.builder()
                .Id("newId")
                .build();

        Customer customer = Customer.builder()
                .id("newId")
                .build();

        when(customerRepo.findByCustomId(request.Id())).thenReturn(null);
        when(customerMapper.toCustomer(request)).thenReturn(customer);
        when(customerRepo.save(any(Customer.class))).thenReturn(customer);

        //When
        String id = customerService.createCustomer(request);

        //Then
        assertNotNull(id);
        assertEquals(id, customer.getId());

    }

    @Test
    void shouldThrowExceptionWhenCustomerIdIsNull(){
        CustomerRequest request = CustomerRequest.builder()
                .Id("non-existent-id")
                .build();

        when(customerRepo.findById(request.Id())).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,()->customerService.updateCustomer(request));

    }

    @Test
    void shouldThrowExceptionWhenCustomerIdIsEmpty(){
        when(customerRepo.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class,()->customerService.findById(null));
    }

}