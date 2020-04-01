package com.jkopay.industry.transport.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    @KafkaListener(id="id01", topics="payment-completed")
    public void doWork(String record) {
        System.out.println(record);
    }
}
