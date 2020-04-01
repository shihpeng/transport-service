package com.jkopay.industry.transport.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class KafkaController {

    private KafkaTemplate<Object, Object> kafkaTemplate;

    @Autowired
    public KafkaController(KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/kafka_send")
    public Object kafkaSend(){

        kafkaTemplate.send("payment-completed", "this is the message");

        return "message sent";
    }
}
