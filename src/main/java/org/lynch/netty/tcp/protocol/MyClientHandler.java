package org.lynch.netty.tcp.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class MyClientHandler extends SimpleChannelInboundHandler<Message> {

    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {

        long length = msg.getLength();
        byte[] content = msg.getContent();
        System.out.printf("服务器接收信息，length:  %d, content: %s \n", length ,
                new String(content, Charset.forName("utf-8")));
        System.out.println("客户端接收消息数量：" + (++this.count));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("ClientHandler发送数据 ");
        for (int i = 0; i < 10; i++) {
            String msg = "吉总大利，今晚吃鸡 ！！！";
            byte[] content = msg.getBytes(Charset.forName("utf-8"));
            int length = content.length;

            // 创建协议包
            Message message = new Message(length,content);
//            ByteBuf byteBuf = Unpooled.copiedBuffer("hello,server" + i, CharsetUtil.UTF_8);
            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
