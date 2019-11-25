package org.lynch.netty.simple;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //读取客户端发送的消息
    // ChannelHandlerContext 上下文对象，包含pipline , channel
    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {

        // 若存在耗时的业务，则可将给消息提交到channel对应的taskQueue中异步执行
        // 解决方案1, 用户自定义普通任务程序
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 10 秒 客户端 ^-^", CharsetUtil.UTF_8));
                }catch (Exception ex){
                    System.out.println("服务器异常: "+ ex.getMessage());
                }
            }
        });

        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(15 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 15 秒 客户端 ^-^", CharsetUtil.UTF_8));
                }catch (Exception ex){
                    System.out.println("服务器异常: "+ ex.getMessage());
                }
            }
        });

        //解决方案 2 ， 将任务放入调度任务中执行，ScheduleTaskQueue
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(15 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 调度任务 客户端 ^-^", CharsetUtil.UTF_8));
                }catch (Exception ex){
                    System.out.println("服务器异常: "+ ex.getMessage());
                }
            }
        },5, TimeUnit.SECONDS);

        System.out.println("服务器读取线程: "+ Thread.currentThread().getName());
//        System.out.println("sever context: "+ ctx);
//        System.out.println("channel 与 pipeline : ");
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline(); // 底层为双向链表
//
//
//        // 将msg转为byteBuffer
//        ByteBuf buffer = (ByteBuf)msg;
//        System.out.println("客户端发送消息： "+ buffer.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址： "+ ctx.channel().remoteAddress());
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
