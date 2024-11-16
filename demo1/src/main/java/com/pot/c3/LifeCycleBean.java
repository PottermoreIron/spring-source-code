package com.pot.c3;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author: Pot
 * @created: 2024-09-17 22:15
 * @description: 生命周期测试类
 */

@Component
public class LifeCycleBean {
    private static final Logger log = LoggerFactory.getLogger(LifeCycleBean.class);

    // 构造[construct]和初始化[init]是两个不同的概念
    // instantiation: 实例化/构造
    // initialization: 初始化
    // 两个方法之间会有属性复制的操作[依赖注入]
    // 调用顺序: 构造 -> 依赖注入 -> 初始化 -> 销毁
    public LifeCycleBean() {
        log.info("LifeCycleBean constructor");
    }

    @Autowired
    public void autowire(@Value("${JAVA_HOME}") String home){
        log.info("autowire: {}", home);
    }

    @PostConstruct
    public void init() {
        log.info("LifeCycleBean init");
    }

    @PreDestroy
    public void destroy() {
        log.info("LifeCycleBean destroy");
    }

}
