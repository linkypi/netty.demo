package org.lynch.nio.gattering;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class GattringServer {
    public static void main(String[] args)throws Exception {
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        InetSocketAddress socketAddress = new InetSocketAddress("192.168.30.8",8899);
        socketChannel.socket().bind(socketAddress);

        int length = 2+3+4;
        ByteBuffer[] buffers = new ByteBuffer[3];

        buffers[0] = ByteBuffer.allocate(2);
        buffers[1] = ByteBuffer.allocate(3);
        buffers[2] = ByteBuffer.allocate(4);

        SocketChannel channel = socketChannel.accept();

        while (true){
            int readCount = 0;
            while (readCount<length){
                long read= channel.read(buffers);
                readCount += read;

                System.out.println("byte read: "+ readCount);
                Arrays.asList(buffers).stream().map(buffer->"position: "+
                        buffer.position()+", limit: "+ buffer.limit())
                .forEach(System.out::println);
            }

            Arrays.asList(buffers).forEach(buffer->{
                buffer.flip() ;
            });

            long writeCount = 0;
            while (writeCount< length){
                long r = channel.write(buffers);
                writeCount+=r;
            }

            Arrays.asList(buffers).forEach(buffer->{
                buffer.clear();
            });

            System.out.println("byteRead: "+ readCount + ", writeCount: "+ writeCount );
        }
    }
}
