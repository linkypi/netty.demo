package org.lynch.nio.selector;

import io.netty.buffer.ByteBuf;
import io.netty.channel.DefaultMaxBytesRecvByteBufAllocator;
import io.netty.handler.codec.compression.FastLzFrameEncoder;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Test {
    public static void main(String[] args)throws Exception {
        List<Integer> list = Arrays.asList(5000, 5001, 5002, 5003, 5004);
        Selector selector = Selector.open();

        for (int i = 0; i < list.size(); i++) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            InetSocketAddress inetSocketAddress = new InetSocketAddress(list.get(i));
            serverSocketChannel.socket().bind(inetSocketAddress);

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("监听端口： " + list.get(i));
        }
        
        while (true){
            int numbers  = selector.select();
            System.out.println("numbers: "+ numbers);

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectedKeys: "+ selectionKeys);

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                if(key.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                    SocketChannel socketChannel = channel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ);
                    iterator.remove();
                    System.out.println("获得客户端连接： "+ socketChannel);
                }

                if(key.isReadable()){
                    SocketChannel socketChannel = (SocketChannel)key.channel();
                    int byteRead = 0;
                    while(true){
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        byteBuffer.clear();

                        int read = socketChannel.read(byteBuffer);
                        if(read<=0){
                            break;
                        }
                        byteBuffer.flip();
                        socketChannel.write(byteBuffer);
                        byteRead += read;
                    }

                    System.out.println("读取字节数： "+ byteRead + ", 来源于： "+ socketChannel);
                    iterator.remove();
                }
            }
        }
    }
}
