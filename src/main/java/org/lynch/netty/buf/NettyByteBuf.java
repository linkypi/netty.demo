package org.lynch.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class NettyByteBuf {
    public static void main(String[] args) {
//        test1();

        ByteBuf byteBuf = Unpooled.copiedBuffer("hello world!", CharsetUtil.UTF_8);
        if(byteBuf.hasArray()){
            byte[] array = byteBuf.array();
            System.out.println(new String(array, Charset.forName("utf-8")));
            System.out.println("bytebuf = "+ byteBuf);
            System.out.println(byteBuf.arrayOffset());
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex());
            System.out.println(byteBuf.capacity());
            System.out.println(byteBuf.readByte());
            System.out.println(byteBuf.readableBytes());
        }
    }

    private static void test1() {
        // 在netty buffer中不需要使用flip进行翻转， 因为其底层维护了writeindex 及 readindex
        ByteBuf buffer = Unpooled.buffer(10);
        for (int i=0;i<10;i++){
            buffer.writeByte(i);
        }
        System.out.println(buffer.readByte());
        System.out.println(buffer.readByte());
        System.out.println(buffer.readByte());
//        for (int i=0;i<buffer.capacity();i++){
//            System.out.println(buffer.getByte(i));
//        }
    }
}
