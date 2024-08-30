package com.Ecom.Customer.customer;

import com.Ecom.Customer.exception.CustomerNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @Test
    void shouldCreateCustomer() throws Exception {

        String response = "Created";

        when(customerService.createCustomer(any(CustomerRequest.class)))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"firstname\": \"value1\", \"lastname\": \"value2\", \"email\": \"value3@email.com\", \"address\": {\"street\": \"123 Main St\", \"houseNumber\": \"A1\", \"zipCode\": \"12345\"}}")
                        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldThrowExceptionWhenRequestNotValid() throws Exception {

        String response = "Created";

        when(customerService.createCustomer(any(CustomerRequest.class)))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"lastname\": \"value2\", \"email\": \"value3@email.com\", \"address\": {\"street\": \"123 Main St\", \"houseNumber\": \"A1\", \"zipCode\": \"12345\"}}")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldUpdateCustomer() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstname\": \"value1\", \"lastname\": \"value2\", \"email\": \"value3@email.com\", \"address\": {\"street\": \"123 Main St\", \"houseNumber\": \"A1\", \"zipCode\": \"12345\"}}"))
                .andExpect(MockMvcResultMatchers.status().isAccepted());

        verify(customerService,times(1)).updateCustomer(any(CustomerRequest.class));

    }

    @Test
    void shouldFindAllCustomers() throws Exception {

        List<CustomerResponse> responses = new ArrayList<>();
        responses.add(new CustomerResponse("newId","FirstName","LastName","Email",new Address()));

        when(customerService.findAllCustomers()).thenReturn(responses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(responses.size()));

        verify(customerService,times(1)).findAllCustomers();

    }

    @Test
    void shouldCheckIfCustomerExists() throws Exception {

        when(customerService.existsById(anyString())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/exits/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(customerService,times(1)).existsById(anyString());
    }

    @Test
    void shouldFindCustomerById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(customerService,times(1)).findById(anyString());

    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() throws Exception {

        when(customerService.findById(anyString())).thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/invalid-id"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Customer not found"));

    }

    @Test
    void shouldThrowExceptionWhenCustomerDoesNotExist() throws Exception {

        when(customerService.findById(anyString())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/invalid-id"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        verify(customerService,times(1)).findById(anyString());
    }

    @Test
    void shouldDeleteCustomer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/customers/1"))
                .andExpect(MockMvcResultMatchers.status().isAccepted());

        verify(customerService,times(1)).deleteCustomer("1");
    }

    @Test
    void shouldThrowExceptionIfCustomerNotFoundToDelete() throws Exception {

        when(customerService.existsById(anyString())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/customers/invaid-id"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Customer not found"));

        verify(customerService,times(0)).deleteCustomer("Invalid_id");
    }

}