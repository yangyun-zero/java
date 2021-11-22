package com.yangyun.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;


/**
 * @Description: SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter 子类
 * @Author yun.Yang
 * @Date 2021/11/18 2:09
 * @Version 1.0
 **/
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 功能描述: 读取客户端数据
     * @param ctx：
     * @param msg：客户端与服务端交互传递数据封装的对象
     * Return: void
     * Author: yun.Yang
     * Date: 2021/11/18 2:10
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        // 判断客户端请求是否为 http 请求
        if (msg instanceof HttpRequest){
            System.out.println("msg 的类型：" + msg);
            System.out.println("客户端地址： "+ ctx.channel().remoteAddress());

            // ctx.channel().pipeline() 客户端请求每次都不一样

            // 通过 HttpObject 获取uri 过滤请求
            HttpRequest request = (HttpRequest) msg;
            HttpHeaders headers = request.headers();
            URI uri = new URI(request.uri());
            System.out.println("来至客户端请求地址：" + uri.getPath());

            // 向客户端写数据
            ByteBuf message = Unpooled.copiedBuffer("我是来至服务器的消息", CharsetUtil.UTF_8);
            // 创建响应对象
            // 设置http协议版本，响应状态为 200，需要回复客户端的数据
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, message);
            HttpHeaders header = response.headers();
            // 回复消息的格式 text
            header.set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            header.set(HttpHeaderNames.CONTENT_LENGTH, message.readableBytes());

            // 会写数据
            ctx.writeAndFlush(response);
        }
    }
}
