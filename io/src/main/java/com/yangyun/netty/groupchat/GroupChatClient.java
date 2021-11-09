package com.yangyun.netty.groupchat;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @Description: 简易版群聊客户端
 * @Author yun.Yang
 * @Date 2021/11/10 1:47
 * @Version 1.0
 **/
public class GroupChatClient {

    private final int PORT = 8888;
    private final String HOST = "127.0.0.1";
    private SocketChannel socketChannel;
    private Selector selector;

    public GroupChatClient () {
        try {
            socketChannel = SocketChannel.open();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(HOST, PORT);
            socketChannel.connect(inetSocketAddress);

            socketChannel.configureBlocking(false);
            selector = Selector.open();
            // 作为客户端，可以只监听从服务端发送来的消息
            socketChannel.register(selector, SelectionKey.OP_READ);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述: 发送消息到服务端
     * @param message：消息内容
     * Return: void
     * Author: yun.Yang
     * Date: 2021/11/10 2:03
     */
    public void sendMessageToServer (String message) {
        try {
            message = socketChannel.getLocalAddress() + "说：" + message;
            socketChannel.write(ByteBuffer.wrap(message.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveOtherClientMessage () {
        try {
            int select = selector.select();
            if (select > 0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()){
                        SocketChannel channel = (SocketChannel)key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        channel.read(buffer);
                        String receiveMessage = new String(buffer.array());
                        System.out.println(receiveMessage.trim());
                    }

                }
                iterator.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        GroupChatClient client = new GroupChatClient();
        new Thread() {
            public void run () {
                // 需要循环接受消息
                while (true) {
                    client.receiveOtherClientMessage();
                }
            }
        }.start();

        Scanner scanner = new Scanner(System.in);



        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            client.sendMessageToServer(s);
        }


    }
}
