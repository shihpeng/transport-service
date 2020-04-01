package com.jkopay.industry.transport.grpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GreeterService extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {

        String name = request.getName();

        String message = "Hello " + name;
        responseObserver.onNext(HelloReply.newBuilder()
                .setMessage(message)
                .build());
        responseObserver.onCompleted();
    }
}
