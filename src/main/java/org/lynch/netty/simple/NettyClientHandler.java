package org.lynch.netty.simple;

import com.sun.org.apache.bcel.internal.ExceptionConstants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    // 当通道就绪就会触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client: "+ ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello , server: o(=•ェ•=)m", CharsetUtil.UTF_8));

    }

    // 读取服务器数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf)msg;
        System.out.println("服务器回复消息： "+ buffer.toString(CharsetUtil.UTF_8));
        System.out.println("服务器地址： "+ ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
