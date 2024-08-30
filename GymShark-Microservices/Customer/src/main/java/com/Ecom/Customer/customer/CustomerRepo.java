package com.Ecom.Customer.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
;import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, String> {

    @Query("SELECT c FROM Customer c WHERE c.id = :id")
    Customer findByCustomId(@Param("id") String id);

    @Query("SELECT c FROM Customer c WHERE c.id = :id")
    CustomerResponse findCustomerResponseById(@Param("id") String id);

}
