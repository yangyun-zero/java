package com.yangyun.io.nio.chat.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description: 服务器
 * @Author yun.Yang
 * @Date 2021/10/25 21:53
 * @Version 1.0
 **/
public class ChatServer {

    private static Charset charset = Charset.forName("UTF-8");

    /**
     * 功能描述: 入口
     * @param ：
     * Return: void
     * Author: yun.Yang
     * Date: 2021/10/25 21:54
     */
    public void startServer () throws Exception {
        // 选择器
        Selector selector = Selector.open();

        // 创建 ServerSocketChannel
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 绑定端口
        ssc.bind(new InetSocketAddress(8080));
        // 设置为非阻塞状态
        ssc.configureBlocking(false);

        // 将通道注册到选择器，监听就绪状态的连接
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        // 循环监听
//        while () { }
        for(;;){ // 此方式编译代码相比while更少
            // 如果为 0 说明没有连接
            int countChannel = selector.select();

            if (countChannel == 0) {
                continue;
            }

            // 获取所有Channel
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            //
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 移除已经拿到的 channel
                iterator.remove();

                // 判断当前 channel 状态
                // 就绪状态
                if (selectionKey.isAcceptable()){
                    this.acceptOperator(ssc, selector);
                }

                // 可读状态
                if (selectionKey.isReadable()) {
                    this.readOperator(selector, selectionKey);
                }
            }
        }
    }

    /**
     * 功能描述: 处理可读状态的连接
     * @param selector：
     * @param selectionKey：
     * Return: void
     * Author: yun.Yang
     * Date: 2021/10/25 22:26
     */
    private void readOperator(Selector selector, SelectionKey selectionKey) throws Exception {
        // 通过 SelectionKey 获取可读状态的 channel
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
        // 创建 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 循环读取数据
        int read = socketChannel.read(byteBuffer);
        String message = "";
        if (read > 0) {
            //切换读模式
            byteBuffer.flip();
            message += charset.decode(byteBuffer);
        }

        // 将通道注册到选择器
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 将此客户端的消息广播到其他连接上的客户端
        if (message.length() > 0){
            System.out.println("来至" + socketChannel.getRemoteAddress() + "的消息：" + message);
            this.castOtherClient(message, selector, socketChannel);
        }
    }

    /**
     * 功能描述: 广播
     * @param message： 
     * @param selector： 
     * @param socketChannel：  
     * Return: void
     * Author: yun.Yang
     * Date: 2021/10/25 23:05
     */
    private void castOtherClient(String message, Selector selector, SocketChannel socketChannel) throws IOException {
        // 获取所有就绪的连接
        Set<SelectionKey> selectionKeys = selector.keys();
        for (SelectionKey selectionKey : selectionKeys){
            //获取每个channel
            Channel tarChannel = selectionKey.channel();
            //不需要给自己发送
            if(tarChannel instanceof SocketChannel && tarChannel != socketChannel) {
                ((SocketChannel)tarChannel).write(Charset.forName("UTF-8").encode(message));
            }
        }

    }

    /**
     * 功能描述: 用于处理就绪状态的连接
     * @param ssc：
     * @param selector：
     * Return: void
     * Author: yun.Yang
     * Date: 2021/10/25 22:10
     */
    private void acceptOperator(ServerSocketChannel ssc, Selector selector) throws Exception {
        // 接入状态，创建SocketChannel
        SocketChannel socketChannel = ssc.accept();
        // 设置为非阻塞模式
        socketChannel.configureBlocking(false);
        // 注册进选择器
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 回复客户端已经连接进入
        socketChannel.write(charset.encode("您已经进入服务，请注意！！！"));
    }

    public static void main(String[] args) throws Exception {
        new ChatServer().startServer();
    }
}
