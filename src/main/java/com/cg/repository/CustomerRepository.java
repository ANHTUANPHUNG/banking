package com.cg.repository;

import com.cg.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    List<Customer> findAllByDeletedFalse();
    Customer findAllById(Long id);


    List<Customer> findAllByIdNot(Long id);
}
