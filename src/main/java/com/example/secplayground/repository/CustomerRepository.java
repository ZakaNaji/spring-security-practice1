package com.example.secplayground.repository;

import com.example.secplayground.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT distinct c FROM Customer c left join fetch c.authorities where c.username= :username")//to not get lazyLoading exception
    Optional<Customer> findCustomerByUsername(String username);
}