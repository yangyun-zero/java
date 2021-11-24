package com.yangyun.netty.groupchat.client;

import com.yangyun.netty.groupchat.client.handler.ClientInitializerHandler;
import com.yangyun.netty.groupchat.constant.ConstantPools;
import com.yangyun.netty.groupchat.entity.LoginUser;
import com.yangyun.netty.groupchat.enums.PlatFormDependentType;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.internal.PlatformDependent;
import org.apache.commons.lang3.StringUtils;

import java.util.Scanner;

/**
 * @Description:
 * @Author yun.Yang
 * @Date 2021/11/23 0:03
 * @Version 1.0
 **/
public class GroupChatNettyClient {

    private EventLoopGroup clientGroup = null;
    private String osName;

    public void init (){
        osName = PlatformDependent.normalizedOs();
        if (StringUtils.startsWithIgnoreCase(PlatFormDependentType.LINUX_OS.getOsType(), osName)){
            clientGroup = new EpollEventLoopGroup();
        } else if (StringUtils.startsWithIgnoreCase(PlatFormDependentType.WIN_OS.getOsType(), osName)){
            clientGroup = new NioEventLoopGroup();
        } else {
            throw new RuntimeException("操作系统匹配异常！");
        }
    }

    public void start (){
        try {

            Bootstrap client = new Bootstrap();
            client.group(clientGroup)
                    .channel(StringUtils.startsWithIgnoreCase(PlatFormDependentType.WIN_OS.getOsType(), osName) ? NioSocketChannel.class : EpollSocketChannel.class)
                    .handler(new ClientInitializerHandler());
            AttributeKey<LoginUser> loginUser = AttributeKey.valueOf(LoginUser.class, "loginUser");


            client.attr(loginUser, new LoginUser());
            ChannelFuture channelFuture = client.connect(ConstantPools.HOST_NAME, ConstantPools.PORT).sync();
            Channel channel = channelFuture.channel();
            channel.attr(loginUser);
            ChannelConfig config = channel.config();
            config.setOption(ChannelOption.valueOf("loginUser"), new LoginUser());

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                channel.writeAndFlush(scanner.nextLine());
            }

        } catch (Exception e) {

        } finally {
            if (clientGroup != null){
                clientGroup.shutdownGracefully();
            }
        }
    }

    public static void main(String[] args) {
        GroupChatNettyClient client = new GroupChatNettyClient();
        client.init();
        client.start();
    }
}