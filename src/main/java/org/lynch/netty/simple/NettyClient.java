package org.lynch.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args)throws Exception {
        // 客户端需要一个事件循环组即可
        NioEventLoopGroup loopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(loopGroup).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyClientHandler()); // 加入自己的处理器
                        }
                    });
            System.out.println("客户端已就绪...");
            ChannelFuture channelFuture = bootstrap.connect("localhost", 6666).sync();
            // 为通道进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            loopGroup.shutdownGracefully();
        }
    }
}
