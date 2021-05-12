package org.lynch.netty.dns;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.dns.*;
import io.netty.util.NetUtil;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Description netty.demo
 * Created by troub on 2021/5/12 9:05
 */
public class DnsResolverTest {
    private static final String QUERY_DOMAIN = "www.lynchpi.com";
    private static final int DNS_SERVER_PORT = 53;
    private static final String DNS_SERVER_HOST = "";//"114.114.114.114";//""8.8.8.8";

    private static void handleQueryResp(DefaultDnsResponse msg) {
        if (msg.count(DnsSection.QUESTION) > 0) {
            DnsQuestion question = msg.recordAt(DnsSection.QUESTION, 0);
            System.out.printf("name: %s%n", question.name());
        }
        for (int i = 0, count = msg.count(DnsSection.ANSWER); i < count; i++) {
            DnsRecord record = msg.recordAt(DnsSection.ANSWER, i);
            if (record.type() == DnsRecordType.A) {
                //just print the IP after query
                DnsRawRecord raw = (DnsRawRecord) record;
                System.out.println(NetUtil.bytesToIpAddress(ByteBufUtil.getBytes(raw.content())));
            }
        }
    }

    static EventLoopGroup group1 = new NioEventLoopGroup();
    static Channel channel;
    static {
        Bootstrap b = new Bootstrap();
        b.group(group1)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new TcpDnsQueryEncoder())
                                .addLast(new TcpDnsResponseDecoder())
                                .addLast(new SimpleChannelInboundHandler<DefaultDnsResponse>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, DefaultDnsResponse msg) {
                                        try {
                                            handleQueryResp(msg);
                                        } finally {
                                            ctx.close();
                                        }
                                    }
                                });
                    }
                });

        try {
            channel = b.connect(DNS_SERVER_HOST, DNS_SERVER_PORT).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args)  throws Exception{

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run()
            {
                System.out.println("Execute Hook.....");
                if(group1!=null){
                    group1.shutdownGracefully();
                    System.out.println("shutdown group gracefully.....");
                }

            }
        }));

//        for(int index = 0;index<100;index++){
//            Thread.sleep(2000);
//            resolve();
//        }
        resolveFromExample();
    }

    private static void resolve() throws Exception {
        int randomID = new Random().nextInt(60000 - 1000) + 1000;
        DnsQuery query = new DefaultDnsQuery(randomID, DnsOpCode.QUERY)
                .setRecord(DnsSection.QUESTION, new DefaultDnsQuestion(QUERY_DOMAIN, DnsRecordType.A));
        if(!channel.isActive()){
           System.out.println("channel is not active ");
        }
        channel.writeAndFlush(query).sync();
        boolean success = channel.closeFuture().await(10, TimeUnit.SECONDS);
        if (!success) {
            System.err.println("dns query timeout!");
            channel.close().sync();
        }
    }

    private static void resolveFromExample() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new TcpDnsQueryEncoder())
                                    .addLast(new TcpDnsResponseDecoder())
                                    .addLast(new SimpleChannelInboundHandler<DefaultDnsResponse>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, DefaultDnsResponse msg) {
                                            try {
                                                handleQueryResp(msg);
                                            } finally {
                                                ctx.close();
                                            }
                                        }
                                    });
                        }
                    });

            final Channel ch = b.connect(DNS_SERVER_HOST, DNS_SERVER_PORT).sync().channel();

            int randomID = new Random().nextInt(60000 - 1000) + 1000;
            DnsQuery query = new DefaultDnsQuery(randomID, DnsOpCode.QUERY)
                    .setRecord(DnsSection.QUESTION, new DefaultDnsQuestion(QUERY_DOMAIN, DnsRecordType.A));
            ch.writeAndFlush(query).sync();
            boolean success = ch.closeFuture().await(10, TimeUnit.SECONDS);
            if (!success) {
                System.err.println("dns query timeout!");
                ch.close().sync();
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}
