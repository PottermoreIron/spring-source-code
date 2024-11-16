package com.pot.c3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author: Pot
 * @created: 2024-09-17 22:14
 * @description: 第三讲测试类
 */

@SpringBootApplication
public class C3Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(C3Application.class, args);
        context.close();
    }
}
