package com.abhishek.service;

import com.abhishek.Employee;
import com.abhishek.EmployeeServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class EmployeeGrpcService extends EmployeeServiceGrpc.EmployeeServiceImplBase {

    @Override
    public void getEmployeeInfo(Empty request, StreamObserver<Employee> responseObserver) {
        responseObserver.onNext(Employee.newBuilder().setName("Abhishek").setSalary(123).build());
        responseObserver.onCompleted();
    }
}
