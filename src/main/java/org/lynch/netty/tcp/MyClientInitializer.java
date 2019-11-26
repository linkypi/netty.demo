package org.lynch.netty.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.lynch.netty.inouthandler.client.ClientHandler;
import org.lynch.netty.inouthandler.common.ByteToLongDecoder;
import org.lynch.netty.inouthandler.common.LongToByteEncoder;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 加入一个出站的handler 对数据进行编码
        pipeline.addLast(new MyClientHandler());
    }
}
