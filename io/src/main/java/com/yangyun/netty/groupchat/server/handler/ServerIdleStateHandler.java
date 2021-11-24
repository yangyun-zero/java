package com.yangyun.netty.groupchat.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @Description:
 * @Author yun.Yang
 * @Date 2021/11/24 23:45
 * @Version 1.0
 **/
public class ServerIdleStateHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;

            switch (event.state()){
                case READER_IDLE:
                    System.out.println("=====我很长时间未写了");
                    break;
            }
        }
    }
}
