package com.pot.c3;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author: Pot
 * @created: 2024-09-17 22:38
 * @description: 自定义Bean后处理器
 */

@Component
public class MyBeanPostProcessor implements InstantiationAwareBeanPostProcessor, DestructionAwareBeanPostProcessor {

    // 利用自定义Bean后处理器，可以在Spring容器的生命周期中的各个阶段进行操作
    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        if ("lifeCycleBean".equals(beanName)) {
            System.out.println(">>>>>>>>>>before destruction");
        }
    }

    // instantiation: 实例化/构造
    // initialization: 初始化
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if ("lifeCycleBean".equals(beanName)) {
            System.out.println(">>>>>>>>>>before instantiation. The returned object will replace the original bean.");
        }
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if ("lifeCycleBean".equals(beanName)) {
            System.out.println(">>>>>>>>>>after instantiation. If false is returned, then the dependency injection phase is skipped.");
            // return false;
        }
        return true;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if ("lifeCycleBean".equals(beanName)) {
            System.out.println(">>>>>>>>>>executed during the dependency injection phase. Example: @Autowired, @Value, etc.");
        }
        return pvs;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("lifeCycleBean".equals(beanName)) {
            System.out.println(">>>>>>>>>>before initialization. The returned object will replace the original bean. Example: @PostConstruct, @ConfigurationProperties, etc.");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if ("lifeCycleBean".equals(beanName)) {
            System.out.println(">>>>>>>>>>after initialization. The returned object will replace the original bean. Example: Proxy, etc.");
        }
        return bean;
    }


}
