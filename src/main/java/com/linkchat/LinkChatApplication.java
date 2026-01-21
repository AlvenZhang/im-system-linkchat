package com.linkchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.linkchat.mapper")
public class LinkChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkChatApplication.class, args);
    }

}