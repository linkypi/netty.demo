package org.lynch.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.SocketAddress;

public class MyServerHanlder extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if(evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent)evt;
            String socketAddress = ctx.channel().remoteAddress().toString()+" 超时事件发生： ";
            String eventType = "读空闲";
            switch (idleStateEvent.state()){
                case READER_IDLE:
                    eventType = "读空闲";
                    break;

                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;

                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }
            System.out.println(socketAddress + eventType);
        }
    }
}


