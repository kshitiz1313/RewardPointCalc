package org.example.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    private String description;
    private Double amount;
    private Date transactionTimeStamp;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getTransactionTimeStamp() {
        return transactionTimeStamp;
    }

    public void setTransactionTimeStamp(Date transactionTimeStamp) {
        this.transactionTimeStamp = transactionTimeStamp;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
