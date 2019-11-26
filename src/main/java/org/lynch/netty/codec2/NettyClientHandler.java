package org.lynch.netty.codec2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Random;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    // 当通道就绪就会触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client: "+ ctx);
//        StudentPOJO.Student student = StudentPOJO.Student.newBuilder().setId(1).setName("lynch").build();
//        ctx.writeAndFlush(student);
        int random = new Random().nextInt(3);
        MyDataInfo.DataInfo data = null;
        if(0==random){
            data = MyDataInfo.DataInfo.newBuilder().setDataType(MyDataInfo.DataInfo.DataType.Student)
                    .setStudent(MyDataInfo.Student.newBuilder().setId(12).setName("jack").build()).build();
        }else{
            data = MyDataInfo.DataInfo.newBuilder().setDataType(MyDataInfo.DataInfo.DataType.Worker)
                    .setWorker(MyDataInfo.Worker.newBuilder().setAge(20).setName("worker").build()).build();
        }
        ctx.writeAndFlush(data);
    }

    // 读取服务器数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf)msg;
        System.out.println("服务器回复消息： "+ buffer.toString(CharsetUtil.UTF_8));
        System.out.println("服务器地址： "+ ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
