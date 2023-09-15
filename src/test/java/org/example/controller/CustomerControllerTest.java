package org.example.controller;

import org.example.model.Customer;
import org.example.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

   @Test
    public void getAllCustomers_ReturnsListOfCustomers() throws Exception {
        Customer firstCustomer = new Customer();
        firstCustomer.setFirstName("Alice");
        firstCustomer.setLastName("johnes");

        Customer secondCustomer = new Customer();
        secondCustomer.setFirstName("Courtney");
        secondCustomer.setLastName("keath");

        List<Customer> customers = Arrays.asList(
               firstCustomer,
                secondCustomer
        );

        when(customerService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("Alice")))
                .andExpect(jsonPath("$[1].firstName", is("Courtney")));
    }

    @Test
    public void createCustomer_CreatesCustomer() throws Exception {
        Customer alice = new Customer();
        alice.setFirstName("Alice");
        alice.setLastName("johnes");

        when(customerService.createCustomer(ArgumentMatchers.any())).thenReturn(alice);
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Alice\"}")
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("Alice")));
    }

    @Test
    public void getCustomerById_ReturnsCustomerWhenFound() throws Exception {
        Customer alice = new Customer();
        alice.setFirstName("Alice");
        alice.setLastName("johnes");

        when(customerService.getCustomer(1L)).thenReturn(alice);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Alice")));
    }

    @Test
    public void getCustomerById_ReturnsNotFoundWhenCustomerNotFound() throws Exception {
        when(customerService.getCustomer(1L)).thenReturn(null);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateCustomer_UpdatesCustomerWhenFound() throws Exception {
        Customer updatedAlice = new Customer();
        updatedAlice.setFirstName("Updated Alice");
        updatedAlice.setLastName("johnes");

        when(customerService.updateCustomer(eq(1L), ArgumentMatchers.any())).thenReturn(updatedAlice);

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Updated Alice\"}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Updated Alice")));
    }

    @Test
    public void updateCustomer_ReturnsNotFoundWhenCustomerNotFound() throws Exception {
        when(customerService.updateCustomer(eq(1L), ArgumentMatchers.any())).thenReturn(null);

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Updated Alice\"}")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCustomer_DeletesCustomerWhenFound() throws Exception {
        when(customerService.deleteCustomerById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteCustomer_ReturnsNotFoundWhenCustomerNotFound() throws Exception {
        when(customerService.deleteCustomerById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNotFound());
    }

}