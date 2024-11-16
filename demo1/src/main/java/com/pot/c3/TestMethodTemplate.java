package com.pot.c3;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Pot
 * @created: 2024-09-18 22:17
 * @description: 测试模板工厂类
 */

public class TestMethodTemplate {

    public static void main(String[] args) {
        MyBeanFactory factory = new MyBeanFactory();
        factory.addBeanPostProcessor(bean -> System.out.println("解析@autowired"));
        factory.addBeanPostProcessor(bean -> System.out.println("解析@Resource"));
        factory.getBean();
    }

    // 静[固定不变的方法]+动[不能确定的方法]
    static class MyBeanFactory {
        public Object getBean(){
            Object bean = new Object();
            System.out.println("构造"+bean);
            System.out.println("依赖注入"+bean);
            for (BeanPostProcessor processor : processors) {
                processor.inject(bean);
            }
            System.out.println("初始化"+bean);
            return bean;
        }
        private List<BeanPostProcessor> processors = new ArrayList<>();
        public void addBeanPostProcessor(BeanPostProcessor processor){
            processors.add(processor);
        }
    }

    interface BeanPostProcessor{
        // 对依赖注入阶段进行扩展
        // 模板方法
        void inject(Object bean);
    }

}
