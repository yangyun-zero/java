package com.yangyun.netty.groupchat.server;

import com.yangyun.netty.groupchat.constant.ConstantPools;
import com.yangyun.netty.groupchat.enums.PlatFormDependentType;
import com.yangyun.netty.groupchat.server.handler.ServerInitializerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.PlatformDependent;
import org.springframework.util.StringUtils;


/**
 * @Description:
 * @Author yun.Yang
 * @Date 2021/11/22 22:28
 * @Version 1.0
 **/
public class GroupChatNettyServer {

    private EventLoopGroup bossGroup = null;

    private EventLoopGroup workerGroup = null;

    private String osName;

    /**
     * 功能描述: 根据操作系统初始化工作组
     * @param ：
     * Return: void
     * Author: yun.Yang
     * Date: 2021/11/22 22:40
     */
    public void init () {
        // 服务器系统
        osName = PlatformDependent.normalizedOs();
        if (StringUtils.startsWithIgnoreCase(PlatFormDependentType.LINUX_OS.getOsType(), osName)){
            bossGroup = new EpollEventLoopGroup(1);
            workerGroup = new EpollEventLoopGroup();
        } else if (StringUtils.startsWithIgnoreCase(PlatFormDependentType.WIN_OS.getOsType(), osName)) {
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
        } else {
            throw new RuntimeException("操作系统匹配异常！");
        }
    }

    public void start (){
        try {

            // 服务器启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 设置启动参数
            serverBootstrap.group(bossGroup, workerGroup)
                            .channel(org.apache.commons.lang3.StringUtils.startsWithIgnoreCase(PlatFormDependentType.WIN_OS.getOsType(), osName) ? NioServerSocketChannel.class : EpollServerSocketChannel.class)
                            .option(ChannelOption.SO_BACKLOG, 100)
                            .childOption(ChannelOption.SO_KEEPALIVE, true)
                            .childHandler(new ServerInitializerHandler());


            serverBootstrap.bind(ConstantPools.PORT)
                .channel().closeFuture().sync();

        } catch (Exception e) {

        } finally {
            if (workerGroup != null){
                workerGroup.shutdownGracefully();
            }
            if (bossGroup != null){
                bossGroup.shutdownGracefully();
            }
        }
    }

    public static void main(String[] args) {
        GroupChatNettyServer server = new GroupChatNettyServer();
        server.init();
        server.start();
    }
}
