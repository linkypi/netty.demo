package org.lynch.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import sun.awt.geom.AreaOp;

public class NettyServer {
    public static void main(String[] args)throws Exception {
        // 创建BossGroup , 仅接受连接请求
        // 两个group都是无线循环
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务器端启动对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // 使用NioServerSocketChannel作为服务器通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道测试对象
                        // 向pipline设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 可使用集合管理socketchannel , 在推送信息时可将业务加入到各个channel对应的NIOEventLoop的
                            // TaskQueue或者 ScheduleTaskQuuee
                            System.out.println("客户端连接：socketchannel hashcode : "+ socketChannel.hashCode());
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            System.out.println("服务器已就绪..");
            // 绑定端口 并同步，生成一个channelfuture对象
            final ChannelFuture channelFuture = serverBootstrap.bind(6666).sync();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                   if(channelFuture.isSuccess()){
                       System.out.println("端口监听成功");
                   }

                }
            });
            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
