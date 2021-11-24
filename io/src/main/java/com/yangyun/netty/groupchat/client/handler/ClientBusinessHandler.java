package com.yangyun.netty.groupchat.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Description:
 * @Author yun.Yang
 * @Date 2021/11/23 22:48
 * @Version 1.0
 **/
public class ClientBusinessHandler extends SimpleChannelInboundHandler<String> {
    /**
     * 功能描述: 处理来至服务端的数据
     * @param ctx：
     * @param msg：
     * Return: void
     * Author: yun.Yang
     * Date: 2021/11/23 22:49
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);
    }
}
