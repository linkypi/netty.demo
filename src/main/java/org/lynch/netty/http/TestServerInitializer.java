package org.lynch.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 向管道加入处理器
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        // 增加自定义处理器
        pipeline.addLast("TestServerHandler",new TestServerHandler());

    }
}
