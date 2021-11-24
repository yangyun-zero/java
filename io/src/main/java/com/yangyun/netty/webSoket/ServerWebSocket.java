package com.yangyun.netty.webSoket;

import com.yangyun.netty.nettythreadmodel.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.bootstrap.ServerBootstrapConfig;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.internal.PlatformDependent;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description:
 * @Author yun.Yang
 * @Date 2021/11/24 23:38
 * @Version 1.0
 **/
public class ServerWebSocket {

    public static void main(String[] args) throws InterruptedException {
// 创建两个 EventLoopGroup
        // bossGroup 只处理 accept 连接请求，真正处理客户端业务会交给 workerGroup
        // bossGroup 和 workerGroup 是两个无线循环
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // 默认线程数为cpu核心数*2
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务端启动对象，并配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 当前操作系统
            String osName = PlatformDependent.normalizedOs();

            // 设置参数
            bootstrap.group(bossGroup, workerGroup)// 设置线程组。第一个为处理连接请求并将连接请求注册到第二个线程出，第二个线程组处理读写操作
                    .channel(StringUtils.startsWithIgnoreCase(osName, "win") ? NioServerSocketChannel.class : EpollServerSocketChannel.class)
//                    .option(ChannelOption.SO_BACKLOG, 128) // 设置连接数
//                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 是否建立长连接
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 给 pipeline 设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 添加http编解码器
                            pipeline.addLast(new HttpServerCodec());
                            // websocket 是以快的方式写，
                            pipeline.addLast(new ChunkedWriteHandler());
                            // http 在传输数据的时候是分段传输，HttpObjectAggregator， 可以将多个断整合
                            // 在浏览器请求时，如果数据太大，就会发生多次http请求
                            pipeline.addLast(new HttpObjectAggregator(8129));
                            // websoket 的数据是以 帧的形式传递
                            // WebSocketServerProtocolHandler 能将http协议升级为ws协议保持长链接
                            // 通过一个状态码 101 进行升级
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            pipeline.addLast(new MyTextWebSocketFrameHandler());

                        }
                    }); // 用作处理客户端请求处理器，可以使用netty提供默认处理器，也可以自定义实现


            System.out.println("server is ready ...");

            // 绑定端口并启动  sync 确保不会阻塞
            ChannelFuture channelFuture = bootstrap.bind(6666)
                    .sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

        //创建两个线程组
//        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//        EventLoopGroup workerGroup = new NioEventLoopGroup(); //8个NioEventLoop
//        try {
//
//            ServerBootstrap serverBootstrap = new ServerBootstrap();
//
//            serverBootstrap.group(bossGroup, workerGroup);
//            serverBootstrap.channel(NioServerSocketChannel.class);
//            serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
//            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
//
//                @Override
//                protected void initChannel(SocketChannel ch) throws Exception {
//                    ChannelPipeline pipeline = ch.pipeline();
//
//                    //因为基于http协议，使用http的编码和解码器
//                    pipeline.addLast(new HttpServerCodec());
//                    //是以块方式写，添加ChunkedWriteHandler处理器
//                    pipeline.addLast(new ChunkedWriteHandler());
//
//                    /*
//                    说明
//                    1. http数据在传输过程中是分段, HttpObjectAggregator ，就是可以将多个段聚合
//                    2. 这就就是为什么，当浏览器发送大量数据时，就会发出多次http请求
//                     */
//                    pipeline.addLast(new HttpObjectAggregator(8192));
//                    /*
//                    说明
//                    1. 对应websocket ，它的数据是以 帧(frame) 形式传递
//                    2. 可以看到WebSocketFrame 下面有六个子类
//                    3. 浏览器请求时 ws://localhost:7000/hello 表示请求的uri
//                    4. WebSocketServerProtocolHandler 核心功能是将 http协议升级为 ws协议 , 保持长连接
//                    5. 是通过一个 状态码 101
//                     */
//                    pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
//
//                    //自定义的handler ，处理业务逻辑
//                    pipeline.addLast(new MyTextWebSocketFrameHandler());
//                }
//            });
//
//            //启动服务器
//            ChannelFuture channelFuture = serverBootstrap.bind(7770).sync();
//            channelFuture.channel().closeFuture().sync();
//
//        }finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
    }
}
