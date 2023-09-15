package org.example.service;

import org.example.model.Customer;
import org.example.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
   private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Long customerId, Customer modifiedCustomer) {
        Optional<Customer> existingCustomer = customerRepository.findById(customerId);
        if(existingCustomer.isPresent()){
            Customer tempCustomer = existingCustomer.get();
            tempCustomer.setCustomerId(modifiedCustomer.getCustomerId());
            tempCustomer.setFirstName(modifiedCustomer.getFirstName());
            tempCustomer.setLastName(modifiedCustomer.getLastName());
            tempCustomer.setTransactions(modifiedCustomer.getTransactions());
            tempCustomer.setPhoneNumber(modifiedCustomer.getPhoneNumber());
            return customerRepository.save(tempCustomer);
        }
        return null;
    }

    @Override
    public boolean deleteCustomerById(Long customerId) {
        Optional<Customer> existingCustomer = customerRepository.findById(customerId);
        if(existingCustomer.isPresent()){
            customerRepository.delete(existingCustomer.get());
            return true;
        }
        return false;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomer(Long customerId) {
        Optional<Customer> existingCustomer = customerRepository.findById(customerId);
        if(existingCustomer.isPresent()){
            return existingCustomer.get();
        }
       return null;
    }
}
