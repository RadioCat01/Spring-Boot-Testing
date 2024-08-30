package com.Ecom.Customer.customer;

import com.Ecom.Customer.exception.CustomerNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerServiceIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private CustomerMapper customerMapper;

    @Test
    @Transactional//to roll back
    void shouldSaveCustomer() {

        CustomerRequest request = CustomerRequest.builder()
                .Id("2")
                .firstname("anyName")
                .lastname("anyLastname")
                .email("anyEmail")
                .address(Address.builder().build())
                .build();



        String id = customerService.createCustomer(request);

        assertEquals(id, request.Id());

        Customer newCustomer = customerMapper.toCustomer(request);

        assertEquals(newCustomer.getFirstname(), request.firstname());

        /*verify methods are not used in here, verify method is intended to use with mocks
        not in real scenarios */
    }

    @Test
    @Transactional
    void shouldUpdateCustomer() {

        CustomerRequest request = CustomerRequest.builder()
                .Id("2")
                .firstname("NewFirstName")
                .build();

        Customer customer = Customer.builder()
                .id("2")
                .firstname("NewFirstName")
                .build();

        customerService.updateCustomer(request);

        Customer changedCustomer = customerRepo.findByCustomId("1");

        assertEquals(changedCustomer.getFirstname(), request.firstname());

    }

    @Test
    void shouldFindAllCustomers() {
       List<CustomerResponse> list = customerService.findAllCustomers();
       assertNotNull(list);
    }

    @Test
    void shouldFindCustomerById() {

        Boolean customer = customerService.existsById("1");

        assertEquals(customer,true);
    }

    @Test
    void shouldFindNoCustomerById() {
        Boolean customer = customerService.existsById("111");

        assertEquals(customer,false);
    }

    @Test
    @Transactional
    void shouldDeleteCustomer() {
        customerService.deleteCustomer("1");

        Boolean deleted = customerService.existsById("1");
        assertEquals(deleted,false);

    }

    @Test
    void shouldThrowExceptionIfCustomerDoesNotExist() {
        assertThrows(CustomerNotFoundException.class,()->{
            customerService.findById("invalid-Id");
        });
    }
}