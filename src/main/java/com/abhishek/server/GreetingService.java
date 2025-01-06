package com.abhishek.server;

import com.example.grpc.GreeterGrpc;
import com.example.grpc.Hello;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class GreetingService extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(Hello.HelloRequest request, StreamObserver<Hello.HelloReply> responseObserver) {
        Hello.HelloReply reply = Hello.HelloReply.newBuilder()
                .setMessage("Hello, " + request.getName())
                .setGreeting("How are you doing today")
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9090)
                .addService(new GreetingService())
                .build()
                .start();
        server.awaitTermination();
    }

}
