package org.lynch.grpc.helloworld;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.lynch.grpc.helloworld.service.GreeterGrpc.GreeterImplBase;
import org.lynch.grpc.helloworld.service.GreeterImpl;

import java.io.IOException;

public class HelloServer {
    public static void main(String[] args) throws InterruptedException, IOException {
        final HelloServer server = new HelloServer();
        server.start();
        server.blockUntilShutdown();
    }

    //定义端口
    private final int port = 50051;
    //服务
    private Server server;

    //启动服务,并且接受请求
    private void start() throws IOException {
        server = ServerBuilder.forPort(port).addService(new GreeterImpl()).build().start();
        System.out.println("服务开始启动-------");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("------shutting down gRPC server since JVM is shutting down-------");
                HelloServer.this.stop();
                System.err.println("------server shut down------");
            }
        });
    }

    //stop服务
    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }
    //server阻塞到程序退出
    private void  blockUntilShutdown() throws InterruptedException {
        if (server!=null){
            server.awaitTermination();
        }
    }

}
