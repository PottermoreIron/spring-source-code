package com.pot.c5.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: Pot
 * @created: 2024/11/23 12:08
 * @description: Bean2
 */
@Component
@Slf4j
public class Bean2 {
    public Bean2() {
        log.info("Bean2 construction.");
    }
}
