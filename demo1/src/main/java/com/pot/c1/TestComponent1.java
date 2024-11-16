package com.pot.c1;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author: Pot
 * @created: 2024-09-12 23:59
 * @description: component测试类1
 */

@Component
@Slf4j
public class TestComponent1 {
    private final ApplicationEventPublisher context;

    @Autowired
    public TestComponent1(ApplicationEventPublisher context) {
        this.context = context;
    }

    /**
     * 注册用户，发布用户注册事件
     */
    public void register() {
        context.publishEvent(new UserRegisteredEvent(this));
        log.info("TestComponent1 register");
    }

}
