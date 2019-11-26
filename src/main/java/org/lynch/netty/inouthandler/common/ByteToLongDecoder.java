package org.lynch.netty.inouthandler.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ByteToLongDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        System.out.println("ByteToLongDecoder被调用..");
        if(in.readableBytes() >= 8){
            out.add(in.readLong()); // 需要判断有8隔字节才可读取一个long
        }else{
            System.out.println("服务器接收到的数据不足8字节，无法读取");
        }
    }
}
