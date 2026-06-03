package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    @Autowired
    private IncentiveService incentiveService;

    public void process(Transaction transaction) {
        // 1. Validate sender exists (scaffold repo returns null if absent)
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        if (sender == null) return;

        // 2. Validate recipient exists
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());
        if (recipient == null) return;

        // 3. Validate sender has enough balance
        if (sender.getBalance() < transaction.getAmount()) return;

        // 4. Task 4: get incentive from external API
        Incentive incentive = incentiveService.getIncentive(transaction);
        float incentiveAmount = incentive.getAmount();

        // 5. Update balances: sender pays the amount; recipient receives amount + incentive
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

        // 6. Persist updated users
        userRepository.save(sender);
        userRepository.save(recipient);

        // 7. Persist the transaction record (with incentive)
        TransactionRecord record = new TransactionRecord(
                sender, recipient, transaction.getAmount(), incentiveAmount);
        transactionRecordRepository.save(record);
    }
}
