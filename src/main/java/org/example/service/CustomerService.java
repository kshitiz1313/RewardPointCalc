package org.example.service;

import org.example.model.Customer;

import java.util.List;

public interface CustomerService {
    Customer createCustomer(Customer customer);
    Customer updateCustomer(Long customerId, Customer customer);
    boolean deleteCustomerById(Long customerId);

    List<Customer> getAllCustomers();
    Customer getCustomer(Long customerId);
}
