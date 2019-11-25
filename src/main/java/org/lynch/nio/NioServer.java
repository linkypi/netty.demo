package org.lynch.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Hello world!
 *
 */
public class NioServer
{
    public static void main( String[] args )throws IOException
    {
        System.out.println( "Hello World!" );
        // 创建serversockerchannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();

        // 绑定端口在服务器监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 将serversocketchannel 注册到selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while(true){
            if(selector.select(1000)==0){
                // 没有事件发生
                System.out.println("服务器等待1秒，无连接..");
                continue;
            }

            // 有事件发生 , 已经获取到关注的事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext()){
                // 获取selectionkey
                SelectionKey key = iterator.next();
                // 根据key对应的通道发生的事件做相应处理
                if(key.isAcceptable()){
                    // 有新客户端连接
                    // 生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //将socketchannel 设置为非阻塞，否则会报错
                    socketChannel.configureBlocking(false);
                    System.out.println("客户端连接成功， 生成了一个socketchannel: "+ socketChannel.hashCode());
                    // 将socketChannel 注册到selector ， 关注事件READ， 同时给socketChannel 关联一个Buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if(key.isReadable()){
                    // 通过key反向获取到对应的channel
                    SocketChannel channel = (SocketChannel)key.channel();
                    // 获取到channel 关联的buffer
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    channel.read(buffer);
                    System.out.println("来自客户端： "+ new String(buffer.array()));
                }

                // 手动从集合中删除key  防止重复操作
                iterator.remove();
            }

        }

    }
}
