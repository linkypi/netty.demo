package org.lynch.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import jdk.nashorn.internal.runtime.GlobalFunctions;

import javax.lang.model.element.VariableElement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GroupChatServerHandler extends SimpleChannelInboundHandler {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //一旦连接 第一个被执行
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        // writeAndFlush 方法会遍历所有的channel 并发送消息给各个channel
        String msg = String.format("%s [客户端] %s 加入群组.\n",formatter.format(LocalDateTime.now()),channel.remoteAddress());
        channelGroup.writeAndFlush(msg);
        channelGroup.add(channel);

    }

    // 表示channel处于活动状态，提示 客户端已上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println(ctx.channel().remoteAddress()+" 上线了 ~ ");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 离线了 ~ ");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" ");
        String msg = String.format("%s [客户端] %s 已离开 ~\n",formatter.format(LocalDateTime.now()),ctx.channel().remoteAddress());
        channelGroup.writeAndFlush(msg);
        System.out.println("channelGroup size : "+ channelGroup.size());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        final Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if (channel != ch) {
                String msgx = String.format("%s [客户端] %s 发送消息: %s \n", formatter.format(LocalDateTime.now()), ctx.channel().remoteAddress(), msg);
                ch.writeAndFlush(msgx);
            } else {
                ch.writeAndFlush("[自己] 发送了消息： " + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
