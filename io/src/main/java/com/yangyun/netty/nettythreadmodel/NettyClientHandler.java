package com.yangyun.netty.nettythreadmodel;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @Description: 自定义客户端处理器
 * @Author yun.Yang
 * @Date 2021/11/17 2:10
 * @Version 1.0
 **/
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 功能描述: 通道准备就绪是触发,并可以发送消息
     * @param ctx：  
     * Return: void
     * Author: yun.Yang
     * Date: 2021/11/17 2:11
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端已经连接");
        // 需要对发送的消息进行编码后才能正常发送
//        ctx.writeAndFlush("2323");
        System.out.println("客户端 channel hashCode：" + ctx.pipeline().channel().hashCode());
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, server: (>^ω^<)喵", CharsetUtil.UTF_8));
    }

    /**
     * 功能描述: 通道有读取事件触发
     * @param ctx：
     * @param msg：服务端会写的消息
     * Return: void
     * Author: yun.Yang
     * Date: 2021/11/17 2:11
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
