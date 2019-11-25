package org.lynch.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

public class TestServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        // 判断msg是不是httpRequest请求
        if (msg instanceof HttpRequest) {

            System.out.println("pipiline: "+ ctx.pipeline().hashCode()+", handler: "+ this.hashCode());
            System.out.println("msg 类型： " + msg.getClass());
            System.out.println("客户端地址： " + ctx.channel().remoteAddress());

            HttpRequest request = (HttpRequest)msg;
            URI uri = new URI(request.uri());
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("过滤 /favicon.ico 请求");
                return;
            }
            // 回复消息到浏览器
            ByteBuf content = Unpooled.copiedBuffer("hello, 我是服务器", CharsetUtil.UTF_8);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            ctx.writeAndFlush(response);
        }
    }
}
