package com.jkopay.industry.transport.dubbo;

import com.jkopay.industry.transport.dubbo.api.Result;

public interface QRCodeValidateService {

     Result<String> validate(String qrCodeString);
}
