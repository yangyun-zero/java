package com.yangyun.netty.nettythreadmodel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Description: 客户端发送消息
 *  发送消息（编码）
 *  接受消息（解码）
 * @Author yun.Yang
 * @Date 2021/11/17 2:05
 * @Version 1.0
 **/
public class NettyClient {

    public static void main(String[] args) throws Exception {
        // 创建时间组
        NioEventLoopGroup clientGroup = new NioEventLoopGroup(1);
//        EpollEventLoopGroup // 针对服务器部署在linux服务器

        // 创建客户端启动类
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(clientGroup)
                .channel(NioSocketChannel.class) // 客户端通道
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 客户端处理器，处理服务器回写消息、异常处理
                        ch.pipeline().addLast(new NettyClientHandler());
                    }
                });

        // 绑定服务端地址并连接
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {

            }
        });

        channelFuture.channel().closeFuture().sync();
    }
}
