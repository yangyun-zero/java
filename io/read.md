# NIO

nio new io(非阻塞io)

## Buffer（缓冲区）

### 概述

1. 在 java io 中负责数据的存取，底层都是数据。用于存取不同数据类型的数据（java 8 种基本数据类型，Boolean 除外）， 根据数据类型的不同，提供了不同的数据类型的缓冲区
2. 通过 allocate() 方法申明一个缓冲区，声明后此时为写数据模式，<font color='red'>非直接缓冲区在jvm内存中</font>

### 核心参数以及方法

#### 四大核心参数

capacity：容量，缓冲区最大存储数据的容量，声明后不能修改

limit：界限；表示缓冲区可以操作数据的大小（limit 后面的空间是无法操作的）初始化时，limit=capacity

```java
public class ByteBuffer{
	public static ByteBuffer allocate(int capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException();
        return new HeapByteBuffer(capacity, capacity);
    }
    
    HeapByteBuffer(int cap, int lim) {            // package-private

        super(-1, 0, lim, cap, new byte[cap], 0);
        /*
        hb = new byte[cap];
        offset = 0;
        */
    }
}
public class Buffer {
     ByteBuffer(int mark, int pos, int lim, int cap,   // package-private
                 byte[] hb, int offset)
    {
        super(mark, pos, lim, cap);
        this.hb = hb;
        this.offset = offset;
    }
    
    Buffer(int mark, int pos, int lim, int cap) {       // package-private
        if (cap < 0)
            throw new IllegalArgumentException("Negative capacity: " + cap);
        this.capacity = cap;
        limit(lim);
        position(pos);
        if (mark >= 0) {
            if (mark > pos)
                throw new IllegalArgumentException("mark > position: ("
                                                   + mark + " > " + pos + ")");
            this.mark = mark;
        }
    }
    
    public final Buffer limit(int newLimit) {
        if ((newLimit > capacity) || (newLimit < 0))
            throw new IllegalArgumentException();
        limit = newLimit; // 初始化时，limit=capacity
        if (position > limit) position = limit;
        if (mark > limit) mark = -1;
        return this;
    }
}
```

position：操作位置，表示缓冲区正在操作数据的位置<font color='red'>多个线程读写一个文件</font>

mark：标记位，记录每次调用get()方法后 position 的值，需要需要调用 reset() 方法才能生效<font color='red'>断电续传</font>

0 <= mark <= position <= limit <= capacity

#### 核心方法

put() 存数据到缓冲区

get() 从缓冲区获取数据

flip() 从写数据模式切换到读数据模式，此时会修改核心参数的值

```java
public class Buffer{
	public final Buffer flip() {
        limit = position;
        position = 0;
        mark = -1;
        return this;
    }
}
```

rewind() 重置核心参数的值为flip() 后的值，这样可以重复读取缓冲区中的数据

**clear**() 清空缓冲区，相当于将核心参数的值重置为生命缓冲区时的次，但是缓冲区还存在数据。此方法<font color='red'>慎用</font>

mark() 标记当前 position 的值并记录

reset() 和 mark() 方法一起使用，可以从mark的位置再次读取数据

slice() 在原始缓冲区的基础上分配一个新的缓冲区，一原缓冲区的capacity和limit。修改子缓冲区的数据原缓冲区的数据会跟着改变

asReadOnlyBuffer() 分配一个只读缓冲区，改变原始缓冲区的值，只读缓冲区的值会跟着改变

#### 缓冲区

##### 非直接缓冲区

通过 allocate() 方法申明一个缓冲区，声明后此时为写数据模式，<font color='red'>非直接缓冲区在jvm内存中</font>

![](src/main/resource/images/%E9%9D%9E%E7%9B%B4%E6%8E%A5%E7%BC%93%E5%86%B2%E5%8C%BA.png)

##### 直接缓冲区

allocateDirect() 方法（<font color='red'>直接缓冲区，将缓冲区建立在物理内存中，可以提高效率</font>），减少了应用和操作系统之间读写时的内容复制，在进行分配和取消分配所需要的成本通常高于非直接缓冲区，由于直接分配在操作系统内存中，安全方面也相对较低。

在创建爱你直接缓冲区的时候，如果分配的大小已经超过 -XX:MaxDirectMemorySize=size 设置的大小，那么会报<font color = 'red'>java.lang.OutOfMemoryError: Direct buffer memory</font>

![](src/main/resource/images/%E7%9B%B4%E6%8E%A5%E7%BC%93%E5%86%B2%E5%8C%BA.jpg)

## 通道（Channel）

完全独立的处理器，用于处理IO操作，用于建立源节点与目标节点连接和缓冲区数据的传输。本身不存储任何数据

### 通道获取

1. java 针对支持通道的类提供了 getChannel() 方法

   本地IO：

   FileInputStream/FileOutputStream/RandomAccessFile

   网络IO：

   Socket/ServerSocket/DatagramSocket

2. 在  jdk1.7 中的 NIO.2 针对各个通道提供了静态方法 open()

   1. 使用内存映射文件的方式 MappedByteBuffer
   2. 通道与通道之间进行传输，通过 FileChannel 的 transferTo()和 transgerFrom()

3. 在 jdk1.7 的 NIO.2 的 Files 工具类的 newByteChannel()

通过通道进行文件的读写，底层使用的就是缓冲股传输数据



