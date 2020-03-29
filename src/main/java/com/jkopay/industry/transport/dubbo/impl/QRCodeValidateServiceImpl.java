package com.jkopay.industry.transport.dubbo.impl;

import com.jkopay.industry.transport.model.message.PaymentMessage;
import com.jkopay.industry.transport.dubbo.QRCodeValidateService;
import com.jkopay.industry.transport.dubbo.api.Result;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.jkopay.industry.transport.dubbo.api.Result.failedResult;
import static com.jkopay.industry.transport.dubbo.api.Result.success;


@Service(interfaceClass = com.jkopay.industry.transport.dubbo.QRCodeValidateService.class)
public class QRCodeValidateServiceImpl implements QRCodeValidateService {

    private KafkaTemplate<Object, Object> kafkaTemplate;

    @Autowired
    public QRCodeValidateServiceImpl(KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Result<String> validate(String qrCodeString) {
        if(isNullOrEmpty(qrCodeString)) {
            return failedResult("The QR code string is empty.");
        }

        if(qrCodeString.charAt(0) % 2 == 0) {
            return failedResult("QR code string validation failed, the first character is illegal.");
        }

        PaymentMessage msg = new PaymentMessage();
        msg.setOrderId("abc-12345");
        msg.setTimestamp(System.currentTimeMillis());
        msg.setDescription("Ready to be completed.");
        kafkaTemplate.send("payment-topic", msg);

        return success();
    }
}
