package com.yangyun.io.nio;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 网络io
 * @Author yun.Yang
 * @Date 2021/10/16 22:42
 * @Version 1.0
 **/
public class SocketChannelTest {

    @Test
    public void testServerSocket () throws Exception {

        ServerSocketChannel ssc = ServerSocketChannel.open();
        InetSocketAddress isa = new InetSocketAddress(8888);
        ByteBuffer bf = ByteBuffer.wrap("yangyun".getBytes());
        ssc.socket().bind(isa);
        // 设置为非阻塞模式
        ssc.configureBlocking(false);

        while(true){
            // 如果不是非阻塞模式，当没有连接进入的时候，会阻塞在此处，只有当有连接进来的时候才会继续往下执行
            SocketChannel accept = ssc.accept();
            if(accept == null){
                System.out.println("此时没有连接进入");
                Thread.sleep(TimeUnit.SECONDS.toSeconds(5000));
            } else {
                System.out.println("有连接进来了"+ssc.socket().getInetAddress());
                bf.flip();
                System.out.println();
                accept.write(bf);
            }
        }
    }

    @Test
    public void testSocketChannel () throws Exception{
        SocketChannel ss = SocketChannel.open();
        ss.connect(new InetSocketAddress("127.0.0.1", 8888));

        ByteBuffer bf = ByteBuffer.allocate(1024);
        bf.put("yangyun".getBytes());

        bf.flip();
        SocketChannel channel = ss.socket().getChannel();
        channel.read(bf);
    }

}
