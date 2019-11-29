package org.lynch.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class GroupChatClient {
    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;
    
    public GroupChatClient()throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST,PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
         username = socketChannel.getLocalAddress().toString().substring(1);
         System.out.println(username+ " is ok ...");

    }

    public void sendInfo(String info){
        info = username + ", 说： "+ info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

     public void readInfo(){
        try{
            int count = selector.select();
            if(count>0){ // 有事件发生
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while(iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if(key.isReadable()){
                        SocketChannel channel =  (SocketChannel)key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        channel.read(buffer);
                        String msg = new String(buffer.array());
                        System.out.println("客户端读取到的消息： "+ msg);
                    }
                }
                iterator.remove(); // 移除当前selectionkey 防止重复操作
            }else{
//                System.out.println("没有可用通道...");
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
     }

    public static void main(String[] args)throws  Exception {
        // 启动客户端
        final GroupChatClient groupChatClient = new GroupChatClient();

        //启动一个线程 每隔3秒读取数据
        new Thread(){
            public void run(){
                while (true){
                    groupChatClient.readInfo();
                    try {
                        Thread.currentThread().sleep(3000);
                    }catch (InterruptedException ex){
                        ex.printStackTrace();
                    }
                }
            }
        }.start();

        // 发送数据到服务器
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String str = scanner.nextLine();
            groupChatClient.sendInfo(str);

        }
    }
}
