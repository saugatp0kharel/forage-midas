package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IncentiveService {

    private final RestTemplate restTemplate = new RestTemplate();

    // Incentive API runs on port 8080
    private static final String INCENTIVE_URL = "http://localhost:8080/incentive";

    public Incentive getIncentive(Transaction transaction) {
        try {
            Incentive incentive = restTemplate.postForObject(
                    INCENTIVE_URL,
                    transaction,
                    Incentive.class
            );
            return incentive != null ? incentive : new Incentive(0);
        } catch (Exception e) {
            // If incentive API is not running, default to 0
            return new Incentive(0);
        }
    }
}
