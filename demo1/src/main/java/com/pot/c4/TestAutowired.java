package com.pot.c4;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;

/**
 * @author: Pot
 * @created: 2024-09-18 23:03
 * @description: 测试autowired
 */

public class TestAutowired {
    public static void main(String[] args) throws Throwable{
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 创建过程, 传入的对象为最终成品, 忽略依赖注入, 初始化等阶段
        beanFactory.registerSingleton("bean2", new Bean2());
        beanFactory.registerSingleton("bean3", new Bean3());
        // 用于获取@Value的值
        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        // 查找哪些属性, 方法使用了@Autowired, 这个称之为InjectionMetadata
        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        processor.setBeanFactory(beanFactory);

        Bean1 bean1 = new Bean1();
        System.out.println(bean1);
        processor.postProcessProperties(null, bean1, "bean1");
        System.out.println(bean1);

    }
}
