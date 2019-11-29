package org.lynch.grpc.helloworld.service;

import org.lynch.grpc.helloworld.HelloReply;
import org.lynch.grpc.helloworld.HelloRequest;

public class GreeterImpl extends GreeterGrpc.GreeterImplBase {
    @Override
    public void sayHello(HelloRequest request,
                         io.grpc.stub.StreamObserver<HelloReply> responseObserver) {
        System.out.println("greeter say Hello...");
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
        responseObserver.onNext(reply);  //onNext()方法向客户端返回结果
        responseObserver.onCompleted();  //告诉客户端这次调用已经完成
    }

    @Override
    public void sayHelloAgain(HelloRequest request,
                              io.grpc.stub.StreamObserver<HelloReply> responseObserver) {
        System.out.println("greeter say Hello again...");
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello again " + request.getName()).build();
        responseObserver.onNext(reply);  //onNext()方法向客户端返回结果
        responseObserver.onCompleted();  //告诉客户端这次调用已经完成
    }
}
