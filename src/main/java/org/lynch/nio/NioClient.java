package org.lynch.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {
    public static void main(String[] args) throws IOException {

        // 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        // 提供服务器端的IP及端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        // 连接服务器
        if(!socketChannel.connect(inetSocketAddress)){
            while (!socketChannel.finishConnect()){
                System.out.println("因连接需要时间， 客户端不会阻塞，可做其他工作...");
            }
        }

        String str = "hello lynch";
        // 包装数据到buffer
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        // 发送数据
        socketChannel.write(buffer);
        System.in.read();


    }
}
