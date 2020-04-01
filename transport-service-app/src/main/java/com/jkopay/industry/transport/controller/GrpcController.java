package com.jkopay.industry.transport.controller;

import com.jkopay.industry.transport.grpc.GreeterGrpc;
import com.jkopay.industry.transport.grpc.HelloReply;
import com.jkopay.industry.transport.grpc.HelloRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GrpcController {

    // Actually we should wrap the gRPC stub into another service for use.
    @GrpcClient("local-grpc-server")
    private GreeterGrpc.GreeterBlockingStub greeterStub;

    @GetMapping("/grpc")
    public Object grpc(@RequestParam("name") String name){

        HelloRequest request = HelloRequest.newBuilder()
                .setName(name)
                .build();
        HelloReply response = greeterStub.sayHello(request);

        return response.getMessage();
    }
}
