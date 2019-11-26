package org.lynch.netty.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.lynch.netty.inouthandler.common.ByteToLongDecoder;
import org.lynch.netty.inouthandler.common.LongToByteEncoder;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 增加入站handler进行解码
        pipeline.addLast(new MyServerHandler());
    }
}
