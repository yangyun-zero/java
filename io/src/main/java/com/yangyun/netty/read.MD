# Netty

Netty 如何实现 I/O 模型

基于非阻塞 I/O 实现

底层依赖的是 JDK NIO 框架的多路复用器 Selector

一个多路复用器 Selector 可以同时轮询多个 Channel，也就是一个线程监听一个 Selector 从而达到监听多个Client的目的

事件分发器（Event Dispatch）

负责将读写事件分发给对应的读写事件处理器（Event Handler）

事件分发器分一下两种模式

Reactor ：采用同步 I/O 

Proactor：采用异步 I/O 

## Neety 整体结构

![](../../../../resource/images/netty-%E6%80%BB%E4%BD%93%E7%BB%93%E6%9E%84.jpg)

### Netty 

## Netty 逻辑架构

![](../../../../resource/images/neety-%E9%80%BB%E8%BE%91%E6%9E%B6%E6%9E%84.jpg)