package com.yangyun.vm;

/**
 * @Description:
 * @Author yun.Yang
 * @Date 2021/11/9 23:15
 * @Version 1.0
 **/
public class TestVm {

    public static void main(String[] args) throws Exception {
        byte [] bytes = new byte[12 * 1024 * 1024];
        System.out.println(Runtime.getRuntime().maxMemory() / 1024 / 1024);
        System.in.read();
    }
}
