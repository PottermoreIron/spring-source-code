package com.pot.c5;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * @author: Pot
 * @created: 2024/11/23 13:17
 * @description: 自定义@Bean后处理器
 */
@Slf4j
public class MyBeanPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        try {
            CachingMetadataReaderFactory cachingMetadataReaderFactory = new CachingMetadataReaderFactory();
            // 获取Config类
            MetadataReader metadataReader = cachingMetadataReaderFactory.getMetadataReader(new ClassPathResource("com/pot/c5/Config.class"));
            // 获取被@Bean标注的方法
            Set<MethodMetadata> methods = metadataReader.getAnnotationMetadata().getAnnotatedMethods(Bean.class.getName());
            for (MethodMetadata method : methods) {
                log.info(method.toString());
                // 获取@Bean注解的参数
                String initMethod = Objects.requireNonNull(method.getAnnotationAttributes(Bean.class.getName())).get("initMethod").toString();
                AbstractBeanDefinition beanDefinition = getAbstractBeanDefinition(method, initMethod);
                if (configurableListableBeanFactory instanceof DefaultListableBeanFactory beanFactory) {
                    // 注册bean
                    beanFactory.registerBeanDefinition(method.getMethodName(), beanDefinition);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static AbstractBeanDefinition getAbstractBeanDefinition(MethodMetadata method, String initMethod) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition();
        // 设置工厂方法
        beanDefinitionBuilder.setFactoryMethodOnBean(method.getMethodName(), "config");
        beanDefinitionBuilder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
        if (!initMethod.isEmpty()) {
            beanDefinitionBuilder.setInitMethodName(initMethod);
        }
        return beanDefinitionBuilder.getBeanDefinition();
    }
}
