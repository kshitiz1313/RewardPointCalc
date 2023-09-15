package org.example.service;

import org.example.dto.TransactionInfoDto;
import org.example.model.Customer;
import org.example.model.Transaction;
import org.example.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction updateTransaction(Long transactionId, Transaction transaction) {
        Optional<Transaction> existingTransaction = transactionRepository.findById(transactionId);
        if(existingTransaction.isPresent()){
            Transaction tempTransaction = existingTransaction.get();
            tempTransaction.setAmount(transaction.getAmount());
            tempTransaction.setCustomer(transaction.getCustomer());
            tempTransaction.setTransactionTimeStamp(transaction.getTransactionTimeStamp());
            tempTransaction.setDescription(transaction.getDescription());
            return transactionRepository.save(tempTransaction);
        }
        return null;
    }

    @Override
    public List<Transaction> getAllTransactions() {

        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> getAllTransactionsByCustomer(Long customerId) {
        return transactionRepository.findByCustomerCustomerId(customerId);
    }

    @Override
    public List<Transaction> getAllTransactionsBetween(Date beginDate, Date endDate) {
        return transactionRepository.findByTransactionTimeStampBetween(beginDate,endDate);
    }

    @Override
    public boolean deleteTransaction(Long transactionId) {

        Optional<Transaction> existingTransaction = transactionRepository.findById(transactionId);
        if(existingTransaction.isPresent()){
            transactionRepository.deleteById(transactionId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Transaction> getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId);
    }

    public List<TransactionInfoDto> calculateTransactions(){
        List<Transaction> allTransactions = transactionRepository.findAll();
        Map<Long, TransactionInfoDto> transactionInfoDtoMap = new HashMap<>();
        for(Transaction eachTransaction:allTransactions){
            Customer customer = eachTransaction.getCustomer();
            Long customerId = customer.getCustomerId();
            if(transactionInfoDtoMap.containsKey(customerId)){
                TransactionInfoDto currentDto = transactionInfoDtoMap.get(customerId);
                int rewardPoints = calculateRewardPoints(eachTransaction.getAmount());
                String month = getMonthNameFromDate(eachTransaction.getTransactionTimeStamp());
                if(currentDto.getPointsByMonth().containsKey(month)){
                    int value = currentDto.getPointsByMonth().get(month);
                    currentDto.getPointsByMonth().put(month,value+rewardPoints);
                } else{
                    currentDto.getPointsByMonth().put(month,rewardPoints);
                }
                currentDto.setTotalPoints(totalPoints(currentDto.getPointsByMonth()));
                transactionInfoDtoMap.put(customerId,currentDto);
            } else {
                int rewardPoints = calculateRewardPoints(eachTransaction.getAmount());
                String month = getMonthNameFromDate(eachTransaction.getTransactionTimeStamp());
                TransactionInfoDto newTransactionInfoDto = new TransactionInfoDto();
                newTransactionInfoDto.setCustomerId(customer.getCustomerId());
                newTransactionInfoDto.setFirstName(customer.getFirstName());
                newTransactionInfoDto.setLastName(customer.getLastName());
                newTransactionInfoDto.setPhoneNumber(customer.getPhoneNumber());
                Map<String, Integer> rewardsByMonth = new HashMap<>();
                rewardsByMonth.put(month,rewardPoints);
                newTransactionInfoDto.setPointsByMonth(rewardsByMonth);
                newTransactionInfoDto.setTotalPoints(totalPoints(rewardsByMonth));
                transactionInfoDtoMap.put(customerId,newTransactionInfoDto);
            }

        }
        return transactionInfoDtoMap.values().stream().toList();
    }

    public static int calculateRewardPoints(double purchaseAmount) {
        int rewardPoints = 0;

        if (purchaseAmount > 100) {
            double amountOver100 = purchaseAmount - 100;
            rewardPoints += (int) (amountOver100 * 2);
            purchaseAmount -= amountOver100;
        }

        if (purchaseAmount > 50) {
            double amountBetween50And100 = purchaseAmount - 50;
            rewardPoints += (int) amountBetween50And100;
        }

        return rewardPoints;
    }


    public static String getMonthNameFromDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
        return sdf.format(date);
    }

    public int totalPoints(Map<String, Integer> rewardsMap){
        int totalPoints = 0;
        for(Integer point: rewardsMap.values()){
            totalPoints = totalPoints + point;
        }
        return totalPoints;
    }
}
