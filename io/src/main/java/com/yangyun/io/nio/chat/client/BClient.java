package com.yangyun.io.nio.chat.client;

/**
 * @Description:
 * @Author yun.Yang
 * @Date 2021/10/25 23:42
 * @Version 1.0
 **/
public class BClient {

    public static void main(String[] args) throws Exception {
        new ChatClient().start("Lily");
    }
}
