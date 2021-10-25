package com.yangyun.io.nio;

import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import sun.java2d.pipe.AAShapePipe;
import sun.util.resources.ga.LocaleNames_ga;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @Description: 选择器使用
 * @Author yun.Yang
 * @Date 2021/10/21 21:50
 * @Version 1.0
 **/
public class TestSelector {

    /**
     * 功能描述: 服务端
     * Author: yun.Yang
     * Date: 2021/10/21 21:57
     */
    @Test
    public void testSelectorServer() throws Exception{
        // 1. 获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 2 切换非阻塞模式
        serverSocketChannel.configureBlocking(false);

        // 3 创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //4 绑定端口号
        serverSocketChannel.bind(new InetSocketAddress(9999));
        
        // 5 创建选择器
        Selector selector = Selector.open();

        // 6 将通道注册到选择器，并设置选择器监听的模式
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


        //7 轮询监听选择器

        while(selector.select() > 0) {
            Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
            // 如果存在连接进来
            while (selectionKeyIterator.hasNext()){
                SelectionKey selectionKey = selectionKeyIterator.next();


                SocketChannel accept = serverSocketChannel.accept();
                // 监听不同模式
                if (selectionKey.isAcceptable()){
                    System.out.println("进来的连接已经就绪");
                    // 获取准备就绪的连接
//                    SocketChannel accept = serverSocketChannel.accept();
                    // 切换为非阻塞模式
                    accept.configureBlocking(false);
                    // 将通道注册到选择器
                    // 切换模式为可读模式，切换后，选择器再次轮询就能获取从客户端传过来的数据
                    // 实际上，客户端只是做了连接服务端，并将数据传输过来
                    accept.register(selector, SelectionKey.OP_READ);
                } else if(selectionKey.isReadable()) {
                    // 读取数据
                    // 1 先获取选择器中客户端发送过来数据的通道
                    SocketChannel channel = (SocketChannel)selectionKey.channel();

                    // 2. 清空缓冲区
                    byteBuffer.clear();
                    // 3. 将通道中数据写入缓冲区
                    while (channel.read(byteBuffer) > 0){
                        //4 切换缓冲区模式为写模式
                        byteBuffer.flip();
                        // 5. 获取数据
                        System.out.println(new String(byteBuffer.array(), 0, byteBuffer.limit()));
                        byteBuffer.clear();
                        System.out.println("validOps：" + channel.validOps());
                        System.out.println(channel.finishConnect());
                    }

                }
            }
            selectionKeyIterator.remove();
        }
    }

    /**
     * 功能描述: 客户端
     * Author: yun.Yang
     * Date: 2021/10/21 21:50
     */
    @Test
    public void testSelectClient() throws Exception{
        // 1. 获取通道
        SocketChannel socketChannel = SocketChannel.open();
        // 2. 绑定server端地址和端口号
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 9999));
        socketChannel.configureBlocking(false);
        // 3. 创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 4. buffer写入数据
        byte b = 1;
        byteBuffer.put(b);
        //5. 设置为非阻塞模式

        byteBuffer.flip();
        // 6. 将缓存区数据写入到通道
        socketChannel.write(byteBuffer);
        // 7. 关闭通道
        byteBuffer.clear();
//        socketChannel.close();
    }

    public static void main(String[] args) {

    }

}
