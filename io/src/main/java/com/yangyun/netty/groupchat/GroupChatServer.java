package com.yangyun.netty.groupchat;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description: 简易版在线群聊
 * 单线程模式：单 Reactor 单线程
 * 一个线程处理所有请求（客户端建立连接、数据的读、写、业务处理等）
 * 在客户端连接数量较多时，将无法支撑会出现性能瓶颈
 * @Author yun.Yang
 * @Date 2021/11/10 1:13
 * @Version 1.0
 **/
public class GroupChatServer {

    // 定义初始化参数
    // 端口
    private final int PORT= 8888;
    // 定义ServerSocketChannel
    private ServerSocketChannel serverSocketChannel;
    // 定义选择器
    private Selector selector;

    // 实现初始化
    public GroupChatServer (){
        try {
            serverSocketChannel = ServerSocketChannel.open();
            // 绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));

            // 设置为非组双色模式
            serverSocketChannel.configureBlocking(false);

            // 将通道注册到选择器 selector，监听 accept 事件
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述: 循环监听客户端
     * @param ：
     * Return: void
     * Author: yun.Yang
     * Date: 2021/11/10 1:53
     */
    public void listen () {
        try {
            // 循环监听
            while (true){
                // 监听客户端是否有连接
                int select = selector.select(1000);
                if (select > 0){
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    // 如果有连接，循环按断当前连接的事件
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();

                        // 如果是处于连接事件
                        if (key.isAcceptable()){
                            // 获取当前连接并注册到 selector,，并且监听其可读事件（也就是当前客户端是否有发消息到服务端）
                            SocketChannel socketChannel = serverSocketChannel.accept();

                            // 设置非阻塞模式
                            socketChannel.configureBlocking(false);

                            // 注册
                            socketChannel.register(selector, SelectionKey.OP_READ);

                            System.out.println(socketChannel.getRemoteAddress() + " online...");
                        }
                        // 如果是可读事件，处理客户端发送过来的消息
                        if (key.isReadable()) {
                            this.readClientMessage(key);
                        }

                        iterator.remove();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述: 读取客户端发送来的消息
     * @param clientKey：  
     * Return: void
     * Author: yun.Yang
     * Date: 2021/11/10 1:52
     */
    public void readClientMessage (SelectionKey clientKey) {
        SocketChannel channel = null;
        try {
            // 根据 selectionKey 反向获取当前客户端 channel
            channel = (SocketChannel) clientKey.channel();
            // 需要将 channel 从 buffer 读取数据
            // 创建 buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.read(buffer);

            String message = new String(buffer.array());

            // 获取客户端发送过来的消息，并实现转发
            System.out.println("接受来至 " + channel.getLocalAddress() + " 消息" + message.trim());
//            channel.register(selector, SelectionKey.OP_READ);

            // 开始转发消息
            this.sendMessage(message, channel);

        } catch (Exception e){
            try {
                // 如果不能正常读消息了(也就是不能从 SelectKey 中获取当前连接了)，该客户端可能已经离线
                System.out.println(channel.getRemoteAddress() + "离线了...");
                // 取消注册
                clientKey.cancel();
                // 关闭通道
                channel.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    /**
     * 功能描述: 群回消息，但是不能给自己发送
     * @param message： 需要群回的消息
     * @param exclude：  需要屏蔽的客户端
     * Return: void
     * Author: yun.Yang
     * Date: 2021/11/10 1:52
     */
    public void sendMessage (String message, SocketChannel exclude) throws Exception {

        // 获取所有连接的客户端
        Set<SelectionKey> keys = selector.keys();
        for (SelectionKey key : keys) {
            Channel targetChannel = key.channel();
            // 需要排除自己，不给自己发消息
            if (targetChannel instanceof SocketChannel && targetChannel != exclude){
                ((SocketChannel) targetChannel).write(ByteBuffer.wrap(message.getBytes()));
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer server = new GroupChatServer();
        server.listen();
    }

}
