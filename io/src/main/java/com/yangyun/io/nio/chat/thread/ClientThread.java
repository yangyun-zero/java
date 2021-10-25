package com.yangyun.io.nio.chat.thread;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description:
 * @Author yun.Yang
 * @Date 2021/10/25 23:37
 * @Version 1.0
 **/
public class ClientThread implements Runnable {

    private static Charset charset = Charset.forName("UTF-8");

    private Selector selector;

    public ClientThread(Selector selector){
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            for (; ; ) { // 此方式编译代码相比while更少
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

                    // 可读状态
                    if (selectionKey.isReadable()) {
                        this.readOperator(selector, selectionKey);
                    }
                }
            }
        } catch (Exception e){

        }
    }

    private void readOperator(Selector selector, SelectionKey selectionKey) throws Exception {
        // 通过 SelectionKey 获取可读状态的 channel
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
        // 创建 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 循环读取数据
        int read = socketChannel.read(byteBuffer);
        String message = "";
        if (read > 0) {
            byteBuffer.flip();
            message += Charset.forName("UTF-8").decode(byteBuffer);
        }

        // 将通道注册到选择器
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 将此客户端的消息广播到其他连接上的客户端
        if (message.length() > 0){
            System.out.println(message);
        }
    }
}
