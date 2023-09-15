package org.example.service;

import org.example.dto.TransactionInfoDto;
import org.example.model.Transaction;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TransactionService {
    Transaction createTransaction(Transaction transaction);
    Transaction updateTransaction(Long transactionId, Transaction transaction);
    List<Transaction> getAllTransactions();
    List<Transaction> getAllTransactionsByCustomer(Long customerId);
    List<Transaction>getAllTransactionsBetween(Date beginDate, Date endDate);

    boolean deleteTransaction(Long transactionId);

    Optional<Transaction> getTransactionById(Long transactionId);
    List<TransactionInfoDto> calculateTransactions();
}
