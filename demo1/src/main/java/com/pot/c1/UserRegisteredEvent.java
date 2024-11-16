package com.pot.c1;

import org.springframework.context.ApplicationEvent;

/**
 * @author: Pot
 * @created: 2024-09-13 23:21
 * @description: 用户注册事件类
 */

public class UserRegisteredEvent extends ApplicationEvent {

    public UserRegisteredEvent(Object source) {
        super(source);
    }
}
