package com.yangyun.io.nio;

import org.junit.Test;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * 功能描述: nio non block io 非阻塞io，双向操作
 * channel（通道：用于连接数据传输的两端） buffer（缓冲区：用于存取数据）
 * @Return:
 * @Author: 手握日月摘星辰，世间无我这般人 - yun.Yang
 * @Date: 2021/9/4 11:16
 */
public class TestBuffer {

    /**
     * 功能描述: Buffer: 缓冲区，在java nio 中负责数据的存取。缓冲区就是数组，用于存取不同数据类型的数据（Java 8种基本数据类型，Boolean 除外）
     * 根据数据类型不同，提供了不用数据类型缓冲区
     * ByteBuffer（最常用）、IntBuffer(4)、CharBuffer(2)、FloatBuffer、ShortBuffer、LongBuffer、DoubleBuffer
     * 通过 allocate() 方法申明一个缓冲区
     * 核心参数
     *  capacity：容量，缓冲区最大存储数据的容量，声明后不能修改
     *  limit：界限；表示缓冲区中可以操作数据的大小（limit后面的数据不能操作）。初始化时：limit = capacity
     *  position：位置，表示缓冲区中正在操作数据的位置
     *  0 <= mark <= position <= limit <= capacity
     *  mark：标记，记录get，调用Reset后 position 的位置；断点续传
     * 核心方法
     *      put 存入数据
     *      get 获取数据
     * @Return: void
     * @Author: 手握日月摘星辰，世间无我这般人 - yun.Yang
     * @Date: 2021/9/4 11:20
     */
    @Test
    public void test1 (){
        String str = "abcde";
        // 申明一个用于存储 byte 类型的缓冲区，1024 字节
        // 此时为写数据模式
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        System.out.println("-----------allocate---------");
        System.out.println(byteBuffer.position()); // 0
        System.out.println(byteBuffer.limit()); // 1024
        System.out.println(byteBuffer.capacity()); // 1024

        // 添加五个字节
        System.out.println("-----------put---------");
        byteBuffer.put(str.getBytes());
        System.out.println(byteBuffer.position()); // 5
        System.out.println(byteBuffer.limit()); // 1024
        System.out.println(byteBuffer.capacity()); // 1024

        // 切换到读数据模式
        System.out.println("-----------flip---------");
        byteBuffer.flip();
        System.out.println(byteBuffer.position()); // 0
        System.out.println(byteBuffer.limit()); // 5
        System.out.println(byteBuffer.capacity()); // 1024

        // 利用get获取缓冲区数据
        System.out.println("-----------get---------");
        byte b = byteBuffer.get();
        System.out.println(b);
        System.out.println(byteBuffer.position()); // 1
        System.out.println(byteBuffer.limit()); // 5
        System.out.println(byteBuffer.capacity()); // 1024

        // rewind ：可重复读取数据
        System.out.println("-----------rewind---------");
        byteBuffer.rewind();
        System.out.println(byteBuffer.position()); // 0
        System.out.println(byteBuffer.limit()); // 5
        System.out.println(byteBuffer.capacity()); // 1024

        // clear: 重置各索引参数到最初值，数据还存在
        System.out.println("-----------clear---------");
        byteBuffer.clear();
        byte b1 = byteBuffer.get();
        System.out.println(b1);
        System.out.println(byteBuffer.position()); // 1
        System.out.println(byteBuffer.limit()); // 1024
        System.out.println(byteBuffer.capacity()); // 1024
    }
}
