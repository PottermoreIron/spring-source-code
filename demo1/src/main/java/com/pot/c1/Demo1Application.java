package com.pot.c1;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class Demo1Application {
	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(Demo1Application.class, args);
		// System.out.println(context.getClass().getName());
		/*
		 * 通过反射获取spring容器中的单例对象
		 */
		Field singletonObjectsField = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
		singletonObjectsField.setAccessible(true);
		ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
		Map<String, Object> map = (Map<String, Object>) singletonObjectsField.get(beanFactory);
		map.entrySet().stream().filter(x -> x.getKey().startsWith("test")).forEach(
				x -> System.out.println(x.getKey() + " : " + x.getValue()));
		/*
		 * applicationContext.getMessage()方法获取国际化信息
		 */
		System.out.println(context.getMessage("hi",null, Locale.CHINA));
		System.out.println(context.getMessage("hi",null, Locale.ENGLISH));

		/*
		 * applicationContext.getResources()方法获取资源文件
		 */
		Resource[] resources = context.getResources("classpath:application.properties");
		for (Resource resource : resources) {
			System.out.println(resource);
		}

		Resource[] resources1 = context.getResources("classpath*:META-INF/spring.factories");
		for (Resource resource : resources1) {
			System.out.println(resource);
		}
		/*
		 * application.getEnvironment()方法获取环境信息, 通过getProperty()方法获取配置信息
		 */
		System.out.println(context.getEnvironment().getProperty("java_home"));
		System.out.println(context.getEnvironment().getProperty("spring.application.name"));

		/*
		 * 通过application.getBean()方法获取bean对象, 调用component1的register()方法
		 *
		 */
		context.getBean(TestComponent1.class).register();
	}

}
