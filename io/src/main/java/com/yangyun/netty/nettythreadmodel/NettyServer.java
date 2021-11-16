package com.yangyun.netty.nettythreadmodel;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.PlatformDependent;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description: 服务端在接口消息的时候，网络通讯，只能通过字节的方式
 *  解码（客户端发送来的消息）-> 业务处理 -> 编码（回写消息）
 * @Author yun.Yang
 * @Date 2021/11/15 23:38
 * @Version 1.0
 **/
public class NettyServer {

    public static void main(String[] args) throws Exception {

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
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置连接数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 是否建立长连接
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 给 pipeline 设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // pipeline 和 bossGroup 管理的客户端连接是相互关联
                            // pipeline 中可以关联多个handler，每个handler可以做响应业务处理
                            // channel 更注重的是对数据读写的处理
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    }); // 用作处理客户端请求处理器，可以使用netty提供默认处理器，也可以自定义实现

            System.out.println("server is ready ...");

            // 绑定端口并启动  sync 确保不会阻塞
            ChannelFuture channelFuture = bootstrap.bind(6668)
                    .sync()
                    ;
            // 监听关闭的通道
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
