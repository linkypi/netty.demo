package org.lynch.netty.inouthandler.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class ClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
       System.out.println("来自服务器的消息： "+ msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("ClientHandler发送数据 ");
        ctx.writeAndFlush(1234L);

        // 1. aseeeedfasdfasdf 16个字节
        // 2. 该处理器的前一个handler是LongToByteEncoder

//        ctx.writeAndFlush(Unpooled.copiedBuffer("aseeeedfasdfasdf", CharsetUtil.UTF_8));


    }
}
