package org.example.controller;

import org.example.model.Transaction;
import org.example.service.TransactionService;
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
import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;
@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    public void testGetAllTransactions() throws Exception {
        when(transactionService.getAllTransactions()).thenReturn(Arrays.asList(new Transaction(), new Transaction()));

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))); // Expecting 2 transactions in the response
    }

    @Test
    public void testGetTransactionById() throws Exception {
        Long transactionId = 1L;
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);

        when(transactionService.getTransactionById(transactionId)).thenReturn(Optional.of(transaction));

        mockMvc.perform(get("/api/transactions/{transactionId}", transactionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId", is(transactionId.intValue())));
    }

    @Test
    public void testGetTransactionByIdNotFound() throws Exception {
        Long transactionId = 1L;

        when(transactionService.getTransactionById(transactionId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/transactions/{transactionId}", transactionId))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testCreateTransaction() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);

        when(transactionService.createTransaction(ArgumentMatchers.any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(transaction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId", is(1)));
    }

    @Test
    public void testUpdateTransaction() throws Exception {
        Long transactionId = 1L;
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setTransactionId(transactionId);

        when(transactionService.updateTransaction(eq(transactionId), ArgumentMatchers.any(Transaction.class))).thenReturn(updatedTransaction);

        mockMvc.perform(put("/api/transactions/{transactionId}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedTransaction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId", is(1)));
    }

    @Test
    public void testUpdateTransactionNotFound() throws Exception {
        Long transactionId = 1L;

        when(transactionService.updateTransaction(eq(transactionId), ArgumentMatchers.any(Transaction.class))).thenReturn(null);

        mockMvc.perform(put("/api/transactions/{transactionId}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new Transaction())))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteTransaction() throws Exception {
        Long transactionId = 1L;

        when(transactionService.deleteTransaction(transactionId)).thenReturn(true);

        mockMvc.perform(delete("/api/transactions/{transactionId}", transactionId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteTransactionNotFound() throws Exception {
        Long transactionId = 1L;

        when(transactionService.deleteTransaction(transactionId)).thenReturn(false);

        mockMvc.perform(delete("/api/transactions/{transactionId}", transactionId))
                .andExpect(status().isNotFound());
    }

    private static String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

}