package com.jkopay.industry.transport.model.message;

import lombok.Data;

@Data
public class PaymentMessage {

    String orderId;
    Long timestamp;
    String description;
}
