package com.pot.c6;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author: Pot
 * @created: 2024/11/24 15:15
 * @description: MyBean
 */
@Slf4j
public class MyBean implements BeanNameAware, ApplicationContextAware, InitializingBean {
    @Override
    public void setBeanName(String name) {
        log.info("bean name is {}", name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("application context is {}", applicationContext);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("initialization");
    }

    @Autowired
    public void myAutowired(ApplicationContext applicationContext) {
        log.info("application context by autowired: {}", applicationContext);
    }

    @PostConstruct
    public void init() {
        log.info("initialization by postConstruct");
    }
}
