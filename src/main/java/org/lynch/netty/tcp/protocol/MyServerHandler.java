package org.lynch.netty.tcp.protocol;
import	java.nio.charset.Charset;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.nio.charset.Charset;
import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<Message> {

    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        long length = msg.getLength();
        byte[] content = msg.getContent();

        System.out.printf("服务器接收信息，length:  %d, content: %s \n", length ,
                new String(content, Charset.forName("utf-8")));

        System.out.println("服务器接收到消息包数量：" +(++count));

        // 回复消息
        String response = UUID.randomUUID().toString();
        byte[] buffer = response.getBytes("utf-8");
        int size = buffer.length;
        Message message = new Message(size, buffer);
        ctx.writeAndFlush(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
