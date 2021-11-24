package com.yangyun.netty.groupchat.server.handler;

import com.yangyun.netty.groupchat.constant.ConstantPools;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description:
 * @Author yun.Yang
 * @Date 2021/11/22 22:59
 * @Version 1.0
 **/
public class ServerBusinessHandler extends SimpleChannelInboundHandler<String> {

    // channel 组，管理所有连接到服务端的客户端 channel
    // GlobalEventExecutor 全部任务处理器
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

    /**
     * 功能描述: 客户端处于连接状态
     * @param ctx：
     * Return: void
     * Author: yun.Yang
     * Date: 2021/11/22 23:05
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("======channelActive...=====");
        super.channelActive(ctx);
    }

    /**
     * 功能描述: 连接出现异常
     * @param ctx：
     * @param cause：
     * Return: void
     * Author: yun.Yang
     * Date: 2021/11/22 23:04
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("======exceptionCaught...=====");
        // 连接出现异常，移除
        Channel channel = ctx.channel();

        this.offLine(channel);

        channelGroup.remove(channel);
    }

    /**
     * 功能描述: 客户端已经连接
     * @param ctx：
     * Return: void
     * Author: yun.Yang
     * Date: 2021/11/22 23:01
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {




        System.out.println("======handlerAdded...=====");
        Channel newChannel = ctx.channel();

        // 每次新进一个连接，提示其他已经连接到服务器端的客户端，有新的连接进入（表示有新的用户上线）
        if (channelGroup.size() > ConstantPools.ZERO_INT){
            channelGroup.writeAndFlush(newChannel.remoteAddress() + "，上线了\\t\\t" + simpleDateFormat.format(new Date()));
        }

        // 将连接到服务端的客户端channel添加到管理 channelGroup 中
        channelGroup.add(newChannel);
    }

    /**
     * 功能描述: 客户端断开连接
     * @param ctx：
     * Return: void
     * Author: yun.Yang
     * Date: 2021/11/22 23:01
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("======handlerRemoved...=====");
        this.offLine(ctx.channel());
    }

    /**
     * 功能描述: 客户端连接处于不活动状态
     * @param ctx：
     * Return: void
     * Author: yun.Yang
     * Date: 2021/11/22 23:11
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("=====channelInactive...=====");
    }

    /**
     * 功能描述: 接受消息,并转发消息到所有处于连接状态的客户端
     * @param ctx：
     * @param msg：
     * Return: void
     * Author: yun.Yang
     * Date: 2021/11/22 23:01
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("======channelRead0...=====");
        Channel channel = ctx.channel();

        // 服务端 -》 客户端 入栈
        if (channelGroup.size() > ConstantPools.ZERO_INT){
            channelGroup.forEach(ch -> {
                // 其他客户端
                if (channel != ch){
                    ch.writeAndFlush(channel.remoteAddress() + "：" + msg);
                }
            });
        }
    }

    private void offLine (Channel channel){
        if (channelGroup.size() > ConstantPools.ZERO_INT){
            channelGroup.writeAndFlush(channel.remoteAddress() + "，下线了\\t\\t" + simpleDateFormat.format(new Date()));
        }
    }
}
