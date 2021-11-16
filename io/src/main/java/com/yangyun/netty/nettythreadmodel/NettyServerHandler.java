package com.yangyun.netty.nettythreadmodel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Description: 自定义handler处理器，需要集成netty提供的 HandlerAdapter
 * 处理客户端发送的数据，并回写数据到客户端
 * @Author yun.Yang
 * @Date 2021/11/17 1:52
 * @Version 1.0
 **/
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 功能描述: 处理客户端发送的消息
     * @param ctx： 上下文，里面包含了通道、管道、客户端地址信息
     * @param msg： 客户端发送的消息
     * Return: void
     * Author: yun.Yang
     * Date: 2021/11/17 1:55
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 收到来至客户端的消息：PooledUnsafeDirectByteBuf(ridx: 0, widx: 26, cap: 1024)
        // 需要对 msg 进行 bytebuf 转换后才能正常显示
        System.out.println("收到来至客户端的消息：" + msg);
        System.out.println("ChannelHandlerContext ctx 包含的信息" + ctx);
    }

    /**
     * 功能描述: 处理完客户端发送消息后 channelRead，执行
     * @param ctx：
     * Return: void
     * Author: yun.Yang
     * Date: 2021/11/17 2:01
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 想客户端发送消息
//        ctx.writeAndFlush()
    }

    /**
     * 功能描述: 如果连接出现异常后处理
     * @param ctx：
     * @param cause：
     * Return: void
     * Author: yun.Yang
     * Date: 2021/11/17 2:02
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("出现异常了。。。");
    }
}
