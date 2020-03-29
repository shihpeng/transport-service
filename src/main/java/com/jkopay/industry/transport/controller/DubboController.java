package com.jkopay.industry.transport.controller;

import com.jkopay.industry.transport.dubbo.QRCodeValidateService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DubboController {

    @Reference(url="dubbo://localhost:20880")
    private QRCodeValidateService qrCodeValidateService;

    @GetMapping("/dubbo")
    public Object dubbo(@RequestParam("qrCode") String qrCodeString){
        return qrCodeValidateService.validate(qrCodeString);
    }
}
