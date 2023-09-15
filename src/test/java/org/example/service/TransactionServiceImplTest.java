package org.example.service;

import org.example.model.Transaction;
import org.example.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createTransaction_ShouldReturnCreatedTransaction() {
        Transaction transactionToCreate = new Transaction();
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transactionToCreate);

        Transaction createdTransaction = transactionService.createTransaction(transactionToCreate);

        assertNotNull(createdTransaction);
    }

    @Test
    public void updateTransaction_WhenTransactionExists_ShouldReturnUpdatedTransaction() {
        Long transactionId = 1L;
        Transaction existingTransaction = new Transaction();
        existingTransaction.setTransactionId(transactionId);

        Transaction modifiedTransaction = new Transaction();
        modifiedTransaction.setTransactionId(transactionId);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(existingTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(modifiedTransaction);

        Transaction updatedTransaction = transactionService.updateTransaction(transactionId, modifiedTransaction);

        assertNotNull(updatedTransaction);
        assertEquals(transactionId, updatedTransaction.getTransactionId());
    }

    @Test
    public void updateTransaction_WhenTransactionDoesNotExist_ShouldReturnNull() {
        Long transactionId = 1L;
        Transaction modifiedTransaction = new Transaction();
        modifiedTransaction.setTransactionId(transactionId);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        Transaction updatedTransaction = transactionService.updateTransaction(transactionId, modifiedTransaction);

        assertNull(updatedTransaction);
    }

    @Test
    public void getAllTransactions_ShouldReturnListOfTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());

        when(transactionRepository.findAll()).thenReturn(transactions);

        List<Transaction> resultTransactions = transactionService.getAllTransactions();

        assertEquals(2, resultTransactions.size());
    }

    @Test
    public void getAllTransactionsByCustomer_ShouldReturnListOfTransactions() {
        Long customerId = 1L;
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());

        when(transactionRepository.findByCustomerCustomerId(customerId)).thenReturn(transactions);

        List<Transaction> resultTransactions = transactionService.getAllTransactionsByCustomer(customerId);

        assertEquals(2, resultTransactions.size());
    }

    @Test
    public void getAllTransactionsBetween_ShouldReturnListOfTransactions() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = sdf.parse("2023-01-01");
        Date endDate = sdf.parse("2023-02-01");

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());

        when(transactionRepository.findByTransactionTimeStampBetween(beginDate, endDate)).thenReturn(transactions);

        List<Transaction> resultTransactions = transactionService.getAllTransactionsBetween(beginDate, endDate);

        assertEquals(2, resultTransactions.size());
    }

    @Test
    public void deleteTransaction_WhenTransactionExists_ShouldReturnTrue() {
        Long transactionId = 1L;
        Transaction existingTransaction = new Transaction();
        existingTransaction.setTransactionId(transactionId);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(existingTransaction));

        boolean deleted = transactionService.deleteTransaction(transactionId);

        assertTrue(deleted);
        verify(transactionRepository, times(1)).deleteById(existingTransaction.getTransactionId());
    }

    @Test
    public void deleteTransaction_WhenTransactionDoesNotExist_ShouldReturnFalse() {
        Long transactionId = 1L;

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        boolean deleted = transactionService.deleteTransaction(transactionId);

        assertFalse(deleted);
        verify(transactionRepository, never()).delete(any());
    }

    @Test
    public void calculateRewardPoints_ShouldCalculateCorrectRewardPoints() {
        int rewardPoints = transactionService.calculateRewardPoints(120.0);

        assertEquals(90, rewardPoints);
    }

    @Test
    public void getMonthNameFromDate_ShouldReturnCorrectMonthName() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2023-09-14");

        String monthName = TransactionServiceImpl.getMonthNameFromDate(date);

        assertEquals("September", monthName);
    }

    @Test
    public void totalPoints_ShouldCalculateTotalPoints() {
        Map<String, Integer> rewardsMap = new HashMap<>();
        rewardsMap.put("January", 10);
        rewardsMap.put("February", 20);

        int totalPoints = transactionService.totalPoints(rewardsMap);

        assertEquals(30, totalPoints);
    }
}