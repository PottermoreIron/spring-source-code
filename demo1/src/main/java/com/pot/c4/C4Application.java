package com.pot.c4;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author: Pot
 * @created: 2024-09-18 22:30
 * @description: Bean后处理器测试类
 */

public class C4Application {
    public static void main(String[] args) {
        // 干净+方便容器
        GenericApplicationContext context = new GenericApplicationContext();
        // 用原始方法注册bean
        context.registerBean("bean1", Bean1.class);
        context.registerBean("bean2", Bean2.class);
        context.registerBean("bean3", Bean3.class);
        context.registerBean("bean4", Bean4.class);

        // 这样才能获取@Value值
        context.getDefaultListableBeanFactory().setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        // 这个context会设置排序, 所以@Resource会在@Autowire之前执行
        // 解析@Autowired和@Value
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);

        // 解析@Resource, @PostConstruct, @PreDestroy
        context.registerBean(CommonAnnotationBeanPostProcessor.class);

        // 解析@ConfigurationProperties
        ConfigurationPropertiesBindingPostProcessor.register(context.getDefaultListableBeanFactory());

        // 执行beanFactory后处理器和bean后处理器
        // 初始化所有单例
        context.refresh();

        System.out.println(context.getBean(Bean4.class));

        context.close();
    }

}
