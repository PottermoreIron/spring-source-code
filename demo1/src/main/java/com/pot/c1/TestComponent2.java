package com.pot.c1;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author: Pot
 * @created: 2024-09-13 00:00
 * @description: component测试类2
 */

@Component
@Slf4j
public class TestComponent2 {
    /**
     * 监听用户注册事件
     */
    @EventListener
    public void receiveUserRegisteredEvent(UserRegisteredEvent event) {
        log.info("TestComponent2 receive user registered event: {}", event);
        log.info("发短信");
    }

}
