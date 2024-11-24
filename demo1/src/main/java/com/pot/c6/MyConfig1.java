package com.pot.c6;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Pot
 * @created: 2024/11/24 15:15
 * @description: MyConfig1
 */
@Configuration
@Slf4j
public class MyConfig1 {

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        log.info("MyConfig1-setApplicationContext start");
    }

    @PostConstruct
    public void init() {
        log.info("MyConfig1-init");
    }

    @Bean
    public BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return beanFactory -> log.info("MyConfig1-beanFactoryPostProcessor start");
    }
}
