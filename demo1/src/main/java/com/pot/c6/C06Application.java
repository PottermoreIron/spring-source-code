package com.pot.c6;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author: Pot
 * @created: 2024/11/24 15:14
 * @description: 第六讲主程序
 */
@Slf4j
public class C06Application {
    public static void main(String[] args) {
        /*
         * Aware接口用于注入一些与容器相关的信息，例如
         * BeanNameAware 注入Bean名字
         * BeanFactoryAware 注入BeanFactory容器
         * ApplicationContextAware 注入ApplicationContext容器
         * EmbeddedValueResolverAware ${}
         **/
        GenericApplicationContext context = new GenericApplicationContext();
//        context.registerBean("myBean", MyBean.class);
//        context.registerBean("myConfig1", MyConfig1.class);
        context.registerBean("myConfig2", MyConfig2.class);
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);
        context.registerBean(CommonAnnotationBeanPostProcessor.class);
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.refresh();
        context.close();
    }
}
