package org.lynch.netty.tcp.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.lynch.netty.heartbeat.MyServer;

import java.util.List;

public class MessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MessageDecoder 被调用... ");
        // 需要将二进制转为字节码  -> Message数据包
        long length = in.readLong();
        byte[] content = new byte[(int)length];
        in.readBytes(content);
        // 封装为Message对象 放入out ,传给下一个handler处理
        Message message = new Message(length, content);
        out.add(message);

    }
}
