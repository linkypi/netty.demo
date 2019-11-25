package org.lynch.netty.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 *
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //读取客户端发送的消息
    // ChannelHandlerContext 上下文对象，包含pipline , channel
    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {

        // 读取从客户端发送的对象
        StudentPOJO.Student student = (StudentPOJO.Student)msg;
        System.out.println("客户端发送消息： id="+ student.getId() + " , name="+student.getName());
        System.out.println("客户端地址： "+ ctx.channel().remoteAddress());
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 对发送的消息编码后发送
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~",CharsetUtil.UTF_8));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
