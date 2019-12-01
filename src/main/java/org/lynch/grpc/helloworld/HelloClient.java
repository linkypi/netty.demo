package org.lynch.grpc.helloworld;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.lynch.grpc.helloworld.service.GreeterGrpc;
import org.lynch.grpc.helloworld.service.HelloReply;
import org.lynch.grpc.helloworld.service.HelloRequest;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloClient {
    private static final Logger logger = Logger.getLogger(HelloClient.class.getName());

    public static void main(String[] args) throws InterruptedException {
        // Access a service running on the local machine on port 50051
        HelloClient client = new HelloClient("127.0.0.1", 50051);
        try {
            String user = "world";
            // Use the arg as the name to greet if provided
            if (args.length > 0) {
                user = args[0];
            }
            client.greet(user);
        } finally {
            client.shutdown();
        }
    }

    private final ManagedChannel channel;
    private final GreeterGrpc.GreeterBlockingStub blockingStub;

    /** Construct client connecting to HelloWorld server at {@code host:port}. */
    public HelloClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build());
    }

    public HelloClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = GreeterGrpc.newBlockingStub(channel);

    }
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /** Say hello to server. */
    public void greet(String name) {
        logger.info("Will try to greet " + name + " ...");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;
        try {
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Greeting: " + response.getMessage());
    }
}
