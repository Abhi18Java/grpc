package com.abhishek.service;

import com.abhishek.Author;
import com.abhishek.BookAuthorServiceGrpc;
import com.google.protobuf.Descriptors;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BookAuthorService {

    @GrpcClient("grpc-abhishek-service")
    BookAuthorServiceGrpc.BookAuthorServiceBlockingStub synchronousClient;

    public Map<Descriptors.FieldDescriptor, Object> getAuthor(int authorId) {
        Author authorBuilder = Author.newBuilder().setAuthorId(authorId).build();
        Author authorResponse = synchronousClient.getAuthor(authorBuilder);
        return authorResponse.getAllFields();
    }
}
