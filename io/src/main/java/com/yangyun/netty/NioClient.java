package com.yangyun.netty;

import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @Description:
 * @Author yun.Yang
 * @Date 2021/11/9 21:44
 * @Version 1.0
 **/
public class NioClient {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        // 创建客户端 channel
        SocketChannel socketChannel = SocketChannel.open();

        // 绑定服务端
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8080);

        // 客户端连接到服务端
        socketChannel.connect(inetSocketAddress);

        // 设置为非阻塞模式
        socketChannel.configureBlocking(false);

        // 开始发送数据
        String message = null;
        while (true){
            message = scanner.nextLine();
            if (StringUtils.equalsIgnoreCase(message, "exit")){
                break;
            }
            // 创建缓存区 wrap 方法会根据实际的字节大小创建
            socketChannel.write(ByteBuffer.wrap(message.getBytes()));
        }
    }
}
