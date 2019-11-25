package org.lynch.nio.zerocopy;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NIOServer {
    public static void main(String[] args)throws  Exception {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7001);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(inetSocketAddress);

        ByteBuffer buffer = ByteBuffer.allocate(4096);
        while (true){
            SocketChannel socketChannel = serverSocketChannel.accept();
            int readCount = 0;
            while (-1!=readCount){
                try{
                    readCount = socketChannel.read(buffer);

                }catch (Exception ex){
                    ex.printStackTrace();
                }
                buffer.rewind();// 倒带，让position回到初始位置，mark作废
            }
        }
    }
}
