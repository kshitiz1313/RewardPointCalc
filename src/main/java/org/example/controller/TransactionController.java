package org.example.controller;

import org.example.dto.TransactionInfoDto;
import org.example.model.Transaction;
import org.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long transactionId) {
        Optional<Transaction> transaction = transactionService.getTransactionById(transactionId);
        if (transaction.isPresent()) {
            return ResponseEntity.ok(transaction.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/report")
    public ResponseEntity<List<TransactionInfoDto>> getTransactionReport() {
        List<TransactionInfoDto> transactions = transactionService.calculateTransactions();
        return ResponseEntity.ok(transactions);
    }
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return ResponseEntity.ok(createdTransaction);
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable Long transactionId,
            @RequestBody Transaction transaction
    ) {
        Transaction updatedTransaction = transactionService.updateTransaction(transactionId, transaction);
        if (updatedTransaction != null) {
            return ResponseEntity.ok(updatedTransaction);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long transactionId) {
        boolean deleted = transactionService.deleteTransaction(transactionId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
