package com.pot.c5;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: Pot
 * @created: 2024/11/23 13:02
 * @description: 自定义ComponentScan后处理器
 */
@Slf4j
public class MyComponentScanPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        try {
            // 寻找某个类的某个注解对象
            ComponentScan annotation = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
            if (annotation != null) {
                for (String basePackage : annotation.basePackages()) {
                    log.info("basePackage : {}", basePackage);
                    // 转换符合类的路径为标准的Resource路径
                    String path = "classpath*:" + basePackage.replace(".", "/") + "/**/*.class";
                    log.info("path : {}", path);
                    // 获取路径下的所以class信息
                    CachingMetadataReaderFactory cachingMetadataReaderFactory = new CachingMetadataReaderFactory();
                    Resource[] resources = new PathMatchingResourcePatternResolver().getResources(path);
                    AnnotationBeanNameGenerator annotationBeanNameGenerator = new AnnotationBeanNameGenerator();
                    for (Resource resource : resources) {
                        log.info("resource : {}", resource);
                        MetadataReader metadataReader = cachingMetadataReaderFactory.getMetadataReader(resource);
                        String className = metadataReader.getClassMetadata().getClassName();
                        // check类是否有@Component注解
                        boolean b = metadataReader.getAnnotationMetadata().hasAnnotation(Component.class.getName());
                        // check类是否有隐含的@Component注解，例如@Controller
                        boolean b1 = metadataReader.getAnnotationMetadata().hasMetaAnnotation(Component.class.getName());
                        log.info("name: {}, isAnnotation: {}, isMetaAnnotation: {}", className, b, b1);

                        if (b || b1) {
                            // 进行Bean的定义
                            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(className).getBeanDefinition();
                            if (configurableListableBeanFactory instanceof DefaultListableBeanFactory beanFactory) {
                                // 生成bean的名字
                                String name = annotationBeanNameGenerator.generateBeanName(beanDefinition, beanFactory);
                                // 注册bean
                                beanFactory.registerBeanDefinition(name, beanDefinition);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
