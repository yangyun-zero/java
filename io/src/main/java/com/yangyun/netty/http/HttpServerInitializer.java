package com.yangyun.netty.http;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Description:
 * @Author yun.Yang
 * @Date 2021/11/18 2:02
 * @Version 1.0
 **/
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        // 添加处理器
        ChannelPipeline pipeline = ch.pipeline();

        // HttpServerCodec netty 提供 http 编解码器  Codec = coder + decode
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());

        // 添加自定义 handler
        pipeline.addLast(new HttpServerHandler());
    }
}
