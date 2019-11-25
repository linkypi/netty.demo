package org.lynch.nio.groupchat;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    public GroupChatServer(){
        try{
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void listen(){
        try{
            while (true){
                int count = selector.select(2000);
                if(count>0){
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        if(key.isAcceptable()){
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress()+ " 上线...");
                        }

                        if(key.isReadable()){
                             read(key);
                        }
                    }

                    // 移除 防止重复操作
                    iterator.remove();
                }else{
//                    System.out.println("等待...");
                }
            }
        }catch (IOException ex){

        }
    }

    private void read(SelectionKey key){
        SocketChannel channel = null    ;
        try {
            channel = (SocketChannel)key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            if(count>0){
                // 将缓冲区的数据转为字符串
                String msg = new String(buffer.array());
                System.out.println("来自客户端的消息： "+ msg);

                sendMsgToOtherClient(msg,channel);
            }
        }catch (IOException e){
            try {
                if(channel==null){
                    return;
                }
                System.out.println(channel.getRemoteAddress()+" 已离线...");
                // 取消注册并关闭通道
                key.cancel();
                channel.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    private void sendMsgToOtherClient(String msg,SocketChannel self)throws IOException{
        // 遍历
        System.out.println("服务器转发消息...");
        // 遍历所有注册到selector上的socketchannel 并排除self
        for (SelectionKey key:selector.keys()){
            // 通过key取出对应的SocketChannel
            Channel targetChannel  = key.channel();
            //排除自身
            if((!(targetChannel instanceof SocketChannel)) || targetChannel == self){
                continue;
            }

            SocketChannel dest = (SocketChannel)targetChannel;
            ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
            //将buffer数据写入通道中
            dest.write(buffer);
        }
    }

    public static void main(String[] args) {
        // 创建服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
