package com.pot.c4;

import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.StringValueResolver;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
        // 用于解析${}
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders);
        // 查找哪些属性, 方法使用了@Autowired/@Value, 这个称之为InjectionMetadata
        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        processor.setBeanFactory(beanFactory);

        Bean1 bean1 = new Bean1();
        /*
         System.out.println(bean1);
         processor.postProcessProperties(null, bean1, "bean1");
         System.out.println(bean1);
         **/

        Method findAutowiringMetadata = AutowiredAnnotationBeanPostProcessor.class.getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
        findAutowiringMetadata.setAccessible(true);
        // 查找bean1中哪些属性, 方法使用了@Autowired/@Value
        InjectionMetadata injectionMetadata = (InjectionMetadata) findAutowiringMetadata.invoke(processor, "bean1", Bean1.class, null);
        System.out.println(injectionMetadata);

        // 调用InjectionMetadata的inject方法进行依赖注入，注入时按类型从哈找值
        injectionMetadata.inject(bean1, "bean1", null);
        System.out.println(bean1);

        // 按类型查找Bean3
        Field bean3 = Bean1.class.getDeclaredField("bean3");
        DependencyDescriptor dd1 = new DependencyDescriptor(bean3, false);
        Object o1 = beanFactory.doResolveDependency(dd1, null, null, null);
        System.out.println(o1);

        // 按方法查找Bean2
        Method setBean2 = Bean1.class.getDeclaredMethod("setBean2", Bean2.class);
        DependencyDescriptor dd2 = new DependencyDescriptor(new MethodParameter(setBean2, 0), false);
        Object o2 = beanFactory.doResolveDependency(dd2, null, null, null);
        System.out.println(o2);

        // 按方法查找home
        Method setHome = Bean1.class.getDeclaredMethod("setHome", String.class);
        DependencyDescriptor dd3 = new DependencyDescriptor(new MethodParameter(setHome, 0), false);
        Object o3 = beanFactory.doResolveDependency(dd3, null, null, null);
        System.out.println(o3);

    }
}
