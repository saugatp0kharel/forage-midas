package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {

    private final UserRepository userRepository;

    @Autowired
    private TransactionService transactionService;

    public DatabaseConduit(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Used by the test harness (UserPopulator) to seed users.
    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    // Task 2: consume transactions from Kafka and hand them to the service layer.
    @KafkaListener(topics = "${general.kafka-topic}")
    public void listen(Transaction transaction) {
        transactionService.process(transaction);
    }
}
