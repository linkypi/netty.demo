package org.lynch.netty.inouthandler.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Arrays;
import java.util.List;

public class TestClient {
    public static void main(String[] args)throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try{

//           Long a = 1212L;
//            List<String> list = Arrays.asList("asdf", "wer");
//            boolean ret = isinstance(list);

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new ClientInitializer());
            ChannelFuture channelFuture = bootstrap.connect("localhost", 9000).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }

    public static boolean isinstance(Object obj){
        boolean rs;
        if (obj == null) {
            rs= false;
        } else {
            try {
                String temp = (String) obj;
                rs= true;
            } catch (ClassCastException e) {
                rs = false;
            }
        }
        return rs;
    }
}
