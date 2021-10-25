package com.yangyun.io.nio.chat.client;

import com.yangyun.io.nio.chat.thread.ClientThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @Description:
 * @Author yun.Yang
 * @Date 2021/10/25 23:33
 * @Version 1.0
 **/
public class ChatClient {

    private static Charset charset = Charset.forName("UTF-8");

    public void start(String userName) throws Exception {
        //
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));
        socketChannel.configureBlocking(false);

        // 接受服务端消息
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ);
        new Thread(new ClientThread(selector)).start();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()){
            socketChannel.write(Charset.forName("UTF-8").encode(userName + "：" +scanner.nextLine()));
        }
    }

    public static void main(String[] args) throws Exception {
        new ChatClient().start("Lucy");
    }
}
