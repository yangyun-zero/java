package com.yangyun.config;

import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Collections;

/**
 * @author yangyun
 * @Description:
 * @date 2020/10/30 9:32
 */
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {

    @Value("${mybatis-plus.blockattacksqlparser.enabled}")
    private Boolean isBlockAttackSqlParser;

    @Bean
    public PaginationInterceptor paginationInterceptor (){
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        if (isBlockAttackSqlParser){
            paginationInterceptor.setSqlParserList(Collections.singletonList(new BlockAttackSqlParser()));
        }
        return paginationInterceptor;
    }

}
