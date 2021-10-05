package com.yangyun.io.nio;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @Description: 通道：用于建立源节点与目标节点的连接，并对缓冲区数据进行传输，本身并不能传输数据，需要结合缓冲区一起使用\
 * 通过 getChannel() 方法获取
 * 1. 本地IO：
 *      FileInputStream/FileOutputStream/RandomAccessFile
 * 2. 网络IO：
 *      Socket/ServerSocket/DatagramSocket
 * @Author yun.Yang
 * @Date 2021/10/3 18:56
 * @Version 1.0
 **/
public class TestChannel {

    /**
     * 功能描述: 分散读取、聚集写入
     *  分散读取：将通道中的数据分散到多个缓冲区
     *  聚集写入：将多个缓冲区的数据聚集到通道中
     * @param ：
     * Return: void
     * Author: yun.Yang
     * Date: 2021/10/5 21:21
     */
    @Test
    public void testScatterAndGather() throws IOException {
        // 适合读文本，
        // 也可以将多个ByteBuffer 中的数据聚集到一个ByteBuffer 中(这样如果是音频，好像是打不开的)而且，如果接受的byte大小和原始大小不一致还会报错
        RandomAccessFile raf = new RandomAccessFile("1.jpg", "rw");
        // 获取文件大小（字节长度）
        long length = raf.length();
        System.out.println(length);
        FileChannel channel = raf.getChannel();
        ByteBuffer bb1 = ByteBuffer.allocate(124700);
        ByteBuffer bb2 = ByteBuffer.allocate(51);
        ByteBuffer bbs[] = {bb1, bb2};
        // 将通道中的数据读到多个缓冲区
        channel.read(bbs);

        byte [] bytes = new byte[124751];

        for(ByteBuffer buffer : bbs) {
            // 切换到读取数据模式
            buffer.flip();
            for(byte b :buffer.array()){
                System.out.println(b);
            }
        }


        FileChannel outFileChannel = FileChannel.open(Paths.get("5.jpg"), StandardOpenOption.WRITE, StandardOpenOption.READ,StandardOpenOption.CREATE);
        MappedByteBuffer outByteBuffer = outFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, length);
        outByteBuffer.put(bytes);
    }

    /**
     * 功能描述: 通道与通道之间的数据传输
     * @param ：  
     * Return: void
     * Author: yun.Yang
     * Date: 2021/10/5 20:56
     */
    @Test
    public void testChannelToChannel() throws IOException {
        FileChannel inFileChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
        FileChannel outFileChannel = FileChannel.open(Paths.get("4.jpg"), StandardOpenOption.WRITE, StandardOpenOption.READ,StandardOpenOption.CREATE);
        inFileChannel.transferTo(0, inFileChannel.size(), outFileChannel);
    }


    /**
     * 功能描述: 使用直接缓冲区的方式复制文件（内存映射文件）只支持Byte
     * @param ：  
     * Return: void
     * Author: yun.Yang
     * Date: 2021/10/5 20:11
     */
    @Test
    public void testChannelDirect() throws Exception {
        FileChannel inFileChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
        FileChannel outFileChannel = FileChannel.open(Paths.get("3.jpg"), StandardOpenOption.WRITE, StandardOpenOption.READ,StandardOpenOption.CREATE);

        // 内存映射文件
        MappedByteBuffer inByteBuffer = inFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, inFileChannel.size());
        MappedByteBuffer outByteBuffer = outFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, inFileChannel.size());

        // 直接对缓冲区的数据进行读写
        byte [] bytes = new byte[inByteBuffer.limit()];
        inByteBuffer.get(bytes);
        outByteBuffer.put(bytes);
    }


    /**
     * 功能描述: 读写本地文件，非直接缓冲区
     * @param ：
     * Return: void
     * Author: yun.Yang
     * Date: 2021/10/3 19:26
     */
    @Test
    public void testChannel() throws Exception {
        // 1. 创建文件流，根据文件流获取通道
        FileInputStream fis = new FileInputStream("1.jpg");
        FileOutputStream fos = new FileOutputStream("2.jpg");

        // 获取通道
        FileChannel fisChannel = fis.getChannel();
        FileChannel fosChannel = fos.getChannel();

        // 通道不能存储数据，需要缓冲区, 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 循环读取数据
        while(fisChannel.read(byteBuffer) != -1){
            // 切换缓冲区到读数据模式
            byteBuffer.flip();
            // 开始写数据
            fosChannel.write(byteBuffer);
            // 重置缓冲区读写操作位、
            byteBuffer.clear();
        }
        // 关闭通道、流
        fosChannel.close();
        fisChannel.close();
        fos.close();
        fis.close();
    }
}
