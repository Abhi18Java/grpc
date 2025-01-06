package com.abhishek;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
public class BookAuthorServerService extends BookAuthorServiceGrpc.BookAuthorServiceImplBase{

    @Override
    public void getAuthor(Author request, StreamObserver<Author> responseObserver) {
        TempDB.getAuthorsFromTempDb()
                .stream()
                .filter(author -> author.getAuthorId() == request.getAuthorId())
                .findFirst()
                .ifPresent(responseObserver::onNext);
        responseObserver.onCompleted();
    }
}
