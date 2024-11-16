package com.pot.c2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.Controller;

/**
 * @author: Pot
 * @created: 2024-09-16 23:07
 * @description: ApplicationContext测试类
 */

public class TestApplicationContext {
    private static final Logger logger = LoggerFactory.getLogger(TestApplicationContext.class);

    public static void main(String[] args) {
        // xml如果加了<context:annotation-config/>, 会自动注入工具类bean
        // testClassPathXmlApplicationContext();
        // testFileSystemXmlApplicationContext();
        // principleForCpAndFs();
        // xml不用加<context:annotation-config/>, 也会注入工具类bean
        // testAnnotationConfigApplicationContext();
        testAnnotationConfigServletApplicationContext();

    }



    // 从ClassPath加载xml文件, 较为经典[老]
    private static void testClassPathXmlApplicationContext(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("b01.xml");
        for(String name : context.getBeanDefinitionNames()){
            logger.info("ClassPath-BeanDefinitionName: {}", name);
        }
    }

    // 从FileSystem加载xml文件
    private static void testFileSystemXmlApplicationContext(){
        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("D:\\Me\\Code\\SDE\\Project\\Test\\spring-source-code\\demo1\\src\\main\\resources\\b01.xml");
        for(String name : context.getBeanDefinitionNames()){
            logger.info("FileSystem-BeanDefinitionName: {}", name);
        }
    }

    // // 调用XmlBeanDefinitionReader#loadBeanDefinitions(Resource resource)方法
    private static void principleForCpAndFs(){
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        logger.info("读取之前");
        for (String name : beanFactory.getBeanDefinitionNames()) {
            logger.info("BeanDefinitionName: {}", name);
        }
        // 核心
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        // reader.loadBeanDefinitions(new ClassPathResource("b01.xml"));
        reader.loadBeanDefinitions(new FileSystemResource("D:\\Me\\Code\\SDE\\Project\\Test\\spring-source-code\\demo1\\src\\main\\resources\\b01.xml"));
        logger.info("读取之后");
        for (String name : beanFactory.getBeanDefinitionNames()) {
            logger.info("BeanDefinitionName: {}", name);
        }
    }

    // 通过config类加载bean
    private static void testAnnotationConfigApplicationContext(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        for (String name : context.getBeanDefinitionNames()) {
            logger.info("Annotation-BeanDefinitionName: {}", name);
        }
        System.out.println(context.getBean(Bean2.class).getBean1());
    }

    // 内嵌Tomcat怎么工作
    private static void testAnnotationConfigServletApplicationContext(){
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
    }

    @Configuration
    static class Config{
        @Bean
        public Bean1 bean1(){
            return new Bean1();
        }

        @Bean
        public Bean2 bean2(Bean1 bean1){
            Bean2 bean2 = new Bean2();
            bean2.setBean1(bean1);
            return bean2;
        }

    }

    @Configuration
    static class WebConfig{
        // 至少要加前三个东西
        @Bean
        public ServletWebServerFactory servletWebServerFactory(){
            return new TomcatServletWebServerFactory();
        }

        // 通过设置path, 是满足path要求的所有请求都先进入它
        // 分发请求到控制器
        @Bean
        public DispatcherServlet dispatcherServlet(){
            return new DispatcherServlet();
        }

        // 注册DispatcherServlet到Tomcat服务器
        @Bean
        public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(){
            // path控制什么请求进入DispatcherServlet
            return new DispatcherServletRegistrationBean(dispatcherServlet(), "/");
        }

        @Bean("/hello")
        public Controller controller(){
            return (request, response) -> {
                response.getWriter().write("Hello, World!");
                return null;
            };
        }

    }

    static class Bean1{}

    static class Bean2{
        private Bean1 bean1;
        // xml标签调用set方法
        public void setBean1(Bean1 bean1){
            this.bean1 = bean1;
        }

        public Bean1 getBean1(){
            return bean1;
        }
    }

}
