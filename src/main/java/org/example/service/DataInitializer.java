package org.example.service;


import org.example.model.Customer;
import org.example.model.Transaction;
import org.example.repository.CustomerRepository;
import org.example.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public DataInitializer(CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }
    @Override
    public void run(String... args) throws Exception {
       List<Customer> customers = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Customer customer = new Customer();
            customer.setFirstName("Customer" + i);
            customer.setLastName("Lastname" + i);
            customer.setPhoneNumber("123-456-78" + i);
            customerRepository.save(customer);
            customers.add(customer);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date[] transactionDates = new Date[5];
        try {
            transactionDates[0] = dateFormat.parse("2023-01-10");
            transactionDates[1] = dateFormat.parse("2023-02-15");
            transactionDates[2] = dateFormat.parse("2023-03-20");
            transactionDates[3] = dateFormat.parse("2023-04-25");
            transactionDates[4] = dateFormat.parse("2023-05-30");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (Customer customer : customers) {
            for (int i = 0; i < 5; i++) {
                Transaction transaction = new Transaction();
                transaction.setDescription("Transaction " + i + " for " + customer.getFirstName());
                Random random = new Random();

                double randomDouble = 1 + random.nextDouble() * 999;
                transaction.setAmount(randomDouble); // Varying amounts
                transaction.setTransactionTimeStamp(transactionDates[i]);
                transaction.setCustomer(customer);
                transactionRepository.save(transaction);
            }
        }

    }
}
