package com.pot.c5;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author: Pot
 * @created: 2024/11/23 12:04
 * @description: 第五讲主程序
 */
@Slf4j
public class C05Application {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("config", Config.class);
        /* 直接完成依赖注入
        // 解析@ComponentScan，@Bean，@Import，@ImportResource
        context.registerBean(ConfigurationClassPostProcessor.class);
        // 处理@Mapper
        context.registerBean(MapperScannerConfigurer.class, bd->{
            bd.getPropertyValues().add("basePackage", "com.pot.c5.mapper");
        });
        **/
        // 处理@ComponentScan
        context.registerBean(MyComponentScanPostProcessor.class);
        // 处理@Bean
        context.registerBean(MyBeanPostProcessor.class);
        // 处理@Mapper
        context.registerBean(MyMapperPostProcessor.class);
        context.refresh();
        for(String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        context.close();
    }
}
