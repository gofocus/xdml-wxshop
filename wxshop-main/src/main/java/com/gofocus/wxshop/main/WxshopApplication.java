package com.gofocus.wxshop.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("com.gofocus.wxshop.dao")
public class WxshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxshopApplication.class, args);
    }

}
