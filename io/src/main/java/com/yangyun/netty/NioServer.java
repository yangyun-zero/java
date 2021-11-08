package com.yangyun.netty;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description: SocketServer 网络
 * 基于事件驱动，Selector 通过不同事件监听客户端，对相应时间做不同的业务处理
 * @Author yun.Yang
 * @Date 2021/11/8 23:38
 * @Version 1.0
 **/
public class NioServer {

    public static void main(String[] args) throws Exception {

        // 创建 SocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 绑定监听端口
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));
        // 设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        // 创建选择器
        Selector selector = Selector.open();

        // 将 channel 注册到 selector, 并监听 accept 事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环监听客户端的连接
        while (true){
            // 设置 1秒的阻塞超时时间
            int selectCount = selector.select(1000);

            // 说明此时没有客户端连接
            if (selectCount == 0) {
                System.out.println("now has no any client");
                continue;
            }

            // 有客户端连接
            // 获取所有SelectionKey 集合，可以通过 SelectionKey 反向获取 channel
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            
            // 循环获取此时连接到Server 的Client
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();

                // 需要判断连接到 Server 的Client 的事件
                // 每个客户端第一次连接到 Server 肯定是 accept 事件
                if (key.isAcceptable()) {
                    // 由于是第一次连接到 Server，通过SocketChannel 获取相应的连接
                    SocketChannel acceptChannel = serverSocketChannel.accept();

                    // 将连接到 Server 的客户端 Channel 注册到 Selector，并监听它的可读事件，同时绑定一个缓冲区
                    acceptChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                // 当连接到 Server 的客户端连接发生了 OP_READ 事件，做对应的处理
                if (key.isReadable()) {
                    // 每个 SelectionKey 都是绑定了对应的客户端 Channel 的，可以反向获取对应的 Channel
                    SocketChannel socketChannel = (SocketChannel)key.channel();
                    // acceptChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024)); 这里设置了当前 SelectionKey 关联的 ByteBuffer
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    // 将 socketChannel 中的数据读入到 Buffer 中
                    socketChannel.read(buffer);
                    System.out.println("come from client message " + new String(buffer.array()));
                }

                // 防止重复操作，需要手动删除每次处理完后的 selectionKey、
                keyIterator.remove();
            }

        }
    }
}
