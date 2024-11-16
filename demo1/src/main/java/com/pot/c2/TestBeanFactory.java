package com.pot.c2;

import jakarta.annotation.Resource;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author: Pot
 * @created: 2024-09-14 22:43
 * @description: 测试beanFactory
 */

public class TestBeanFactory {
    public static void main(String[] args) {
        /*
         * beanFactory
         * 不会主动调用beanFactory后处理器
         * 不会主动调用bean后处理器
         * 不会主动初始化单例bean
         * 不会解析#{}和${}
         */
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Config.class)
                .setScope("singleton").getBeanDefinition();
        // 这个时候beanDefinition中的bean1和bean2还没被注册
        beanFactory.registerBeanDefinition("config", beanDefinition);
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println("1 " + beanDefinitionName);
        }
        // 注册注解配置处理器
        // 这个方法也会设置处理器的排序器, 但是不会调用排序器
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        // 这个时候只是注册了注解配置处理器, 并没有调用beanFactory后处理器, 所以beanDefinition中的bean1和bean2还没被注册
        // 但可以看见注解配置处理器
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println("2 " + beanDefinitionName);
        }
        // 调用beanFactory后处理器, 补充bean定义
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values().forEach(
                beanFactoryPostProcessor -> beanFactoryPostProcessor.postProcessBeanFactory(beanFactory)
        );
        // 这个时候beanDefinition中的bean1和bean2才被注册
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println("3 " + beanDefinitionName);
        }

        // 如果直接运行下面这句, 会初始化bean1, 但是bean1中的所有bean都是null
        // 把下面这句注释掉才能拿到bean2, 猜测跟bean生命周期有关
        // System.out.println(beanFactory.getBean(Bean1.class).getBean2());
        // 注册bean后处理器
        // 针对bean的生命周期各个阶段做扩展, 比如@Autowired, @Resource
        // 可以看到AutowiredAnnotationBeanPostProcessor在CommonAnnotationBeanPostProcessor之前
//        beanFactory.getBeansOfType(BeanPostProcessor.class).values().forEach(beanPostProcessor -> {
//                System.out.println("4 "+ beanPostProcessor);
//                beanFactory.addBeanPostProcessor(beanPostProcessor);
//            }
//        );

        // 加了排序, 可以看到AutowiredAnnotationBeanPostProcessor在CommonAnnotationBeanPostProcessor之后
        beanFactory.getBeansOfType(BeanPostProcessor.class).values().stream()
                .sorted(Objects.requireNonNull(beanFactory.getDependencyComparator()))
                .forEach(beanPostProcessor -> {
                    System.out.println("5 "+ beanPostProcessor);
                    beanFactory.addBeanPostProcessor(beanPostProcessor);
                });

        // 默认单例bean先注册到工厂, 但不实例化, 调用的时候才实例化
        // 下面这个方法可以预先实例化单例bean
        // 没有下面这句, 会看到bean1和bean2的构造方法在"-----"之后才被调用
        // 有下面这句, 会看到bean1和bean2的构造方法在"-----"之前就被调用
        beanFactory.preInstantiateSingletons();
        System.out.println("--------------------------");
        System.out.println(beanFactory.getBean(Bean1.class).getBean2());

        System.out.println(beanFactory.getBean(Bean1.class).getInterface1());
    }

    @Configuration
    static class Config {
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }

        @Bean
        public Bean3 bean3() {
            return new Bean3();
        }

        @Bean
        public Bean4 bean4() {
            return new Bean4();
        }

    }
    interface Interface1{}
    static class Bean3 implements Interface1{}
    static class Bean4 implements Interface1{}
    static class Bean1{
        private static final Logger log = LoggerFactory.getLogger(Bean1.class);
        public Bean1() {
            log.info("Bean1 init");
        }


        // autowired编译时会报错, 但因为在Config中注册了Bean1和Bean2, 所以还是会成功注入, 运行也不会报错
        @Autowired
        private Bean2 bean2;

        // autowired默认byType, Resource默认byName

        // autowired和resource都会失败, 因为有两个实现类, 无法确定注入哪个
        // private interface1 interface1;

        // autowired和resource都会成功, 有多个实现类, 按照变量名找类
        // private interface1 bean3;

        // 但resource可以带name指定注入哪个
        // @Resource(name = "bean4"), 优先级比变量名高

        // 如果autowired和resource同时存在, 默认autowired优先级高, 会注入bean3
        // 但如果调用了getDependencyComparator的排序器进行排序, 那么resource优先级高, 会注入bean4
        // 调用排序器会按属性order从小到大排序, 而resource的order比autowired小[2147483645, 2147483644]
        // @Autowired
        // @Resource(name = "bean4")
        // private Interface1 bean3;
        @Autowired
        @Resource(name = "bean4")
        private Interface1 bean3;



        public Bean2 getBean2() {
            return bean2;
        }

        public Interface1 getInterface1() {
            return bean3;
        }



    }

    static class Bean2{
        private static final Logger log = LoggerFactory.getLogger(Bean2.class);
        public Bean2() {
            log.info("Bean2 init");
        }
    }

}
