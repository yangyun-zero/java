package com.yangyun.netty.groupchat.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author yun.Yang
 * @Date 2021/11/22 22:50
 * @Version 1.0
 **/
public class ServerInitializerHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 添加编解码处理器
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new StringEncoder());
        pipeline.addLast(new StringDecoder());

        // 添加自定义业务逻辑处理器
        pipeline.addLast(new ServerBusinessHandler());

        // 添加心跳检测handler
        // 多少秒未读、未写，未读写
        pipeline.addLast(new IdleStateHandler(2,3,4, TimeUnit.SECONDS));

        pipeline.addLast(new ServerIdleStateHandler());
    }
}
