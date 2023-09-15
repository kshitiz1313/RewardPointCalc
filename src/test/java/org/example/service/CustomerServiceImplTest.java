package org.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.model.Customer;
import org.example.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createCustomer_ShouldReturnCreatedCustomer() {
        Customer customerToCreate = new Customer();
        customerToCreate.setFirstName("Alice");

        when(customerRepository.save(any(Customer.class))).thenReturn(customerToCreate);

        Customer createdCustomer = customerService.createCustomer(customerToCreate);

        assertNotNull(createdCustomer);
        assertEquals("Alice", createdCustomer.getFirstName());
    }

    @Test
    public void updateCustomer_WhenCustomerExists_ShouldReturnUpdatedCustomer() {
        Long customerId = 1L;
        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(customerId);
        existingCustomer.setFirstName("Alice");

        Customer modifiedCustomer = new Customer();
        modifiedCustomer.setCustomerId(customerId);
        modifiedCustomer.setFirstName("Updated Alice");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(modifiedCustomer);

        Customer updatedCustomer = customerService.updateCustomer(customerId, modifiedCustomer);

        assertNotNull(updatedCustomer);
        assertEquals("Updated Alice", updatedCustomer.getFirstName());
    }

    @Test
    public void updateCustomer_WhenCustomerDoesNotExist_ShouldReturnNull() {
        Long customerId = 1L;
        Customer modifiedCustomer = new Customer();
        modifiedCustomer.setCustomerId(customerId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Customer updatedCustomer = customerService.updateCustomer(customerId, modifiedCustomer);

        assertNull(updatedCustomer);
    }

    @Test
    public void deleteCustomerById_WhenCustomerExists_ShouldReturnTrue() {
        Long customerId = 1L;
        Customer existingCustomer = new Customer();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        boolean deleted = customerService.deleteCustomerById(customerId);

        assertTrue(deleted);
        verify(customerRepository, times(1)).delete(existingCustomer);
    }

    @Test
    public void deleteCustomerById_WhenCustomerDoesNotExist_ShouldReturnFalse() {
        Long customerId = 1L;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        boolean deleted = customerService.deleteCustomerById(customerId);

        assertFalse(deleted);
        verify(customerRepository, never()).delete(any());
    }

    @Test
    public void getAllCustomers_ShouldReturnListOfCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer());
        customers.add(new Customer());

        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> resultCustomers = customerService.getAllCustomers();

        assertEquals(2, resultCustomers.size());
    }

    @Test
    public void getCustomer_WhenCustomerExists_ShouldReturnCustomer() {
        Long customerId = 1L;
        Customer existingCustomer = new Customer();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        Customer retrievedCustomer = customerService.getCustomer(customerId);

        assertNotNull(retrievedCustomer);
    }

    @Test
    public void getCustomer_WhenCustomerDoesNotExist_ShouldReturnNull() {
        Long customerId = 1L;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Customer retrievedCustomer = customerService.getCustomer(customerId);

        assertNull(retrievedCustomer);
    }

}