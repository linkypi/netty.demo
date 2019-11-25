package org.lynch.nio.zerocopy;

import javax.print.attribute.standard.Sides;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args)throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost",7001));
        String filename = "G:\\java\\nettydemo\\src\\main\\22.zip";
        FileChannel channel = new FileInputStream(filename).getChannel();
        long startTime = System.currentTimeMillis();
        // 在Linux下，一个transferTo方法就可以完成传输
        // 在window下一次调用transferTo只能发送8M, 需要分段传输

        long total = 0;
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")){
            System.out.println("当前操作系统为：Windows");
            total = sendInWins(socketChannel, channel);
        }else{
            System.out.println("当前操作系统为：Linux");
            total = channel.transferTo(0, channel.size(), socketChannel);
        }
        System.out.printf("发送的总字节数：%s, 耗时： %s", total, System.currentTimeMillis() - startTime);
    }

    private static long sendInWins(SocketChannel socketChannel, FileChannel channel) throws IOException {
        long total = 0;
        final int SizePerSegment = 8 * 1024 * 1024;
        int segments = (int) Math.ceil(channel.size() / (SizePerSegment*1.0));
        for (int index = 0; index < segments; index++) {
            long transferCount = SizePerSegment;
            if (index == segments - 1) {
                transferCount = channel.size() - SizePerSegment * index;
            }
            total += channel.transferTo(index * SizePerSegment, transferCount, socketChannel);
        }
        return total;
    }
}
