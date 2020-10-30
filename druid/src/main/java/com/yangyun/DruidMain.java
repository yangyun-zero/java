package com.yangyun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author yangyun
 * @Description:
 *  1. druid 集成wallfilter 实现对 sql 注入拦截实现
 *  2. 使用mybatisplus 实现
 * @date 2020/10/29 16:11
 */
@ComponentScan(basePackages = {"com.yangyun.*"})
@MapperScan({"com.yangyun.mapper"})
@SpringBootApplication
public class DruidMain {
    public static void main(String[] args) {
        SpringApplication.run(DruidMain.class);
    }
}
