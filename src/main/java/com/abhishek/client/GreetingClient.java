package com.abhishek.client;

import com.example.grpc.GreeterGrpc;
import com.example.grpc.Hello;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);
        Hello.HelloReply response = stub.sayHello(Hello.HelloRequest.newBuilder().build());

        System.out.println("Response: " + response.getMessage());
        channel.shutdown();

    }
}
