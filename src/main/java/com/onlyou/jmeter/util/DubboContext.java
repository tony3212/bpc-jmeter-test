package com.onlyou.jmeter.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * DubboContext
 *
 * @author HQH
 */
public class DubboContext {
    private static DubboContext instance = new DubboContext();
    private ApplicationContext context;

    private DubboContext() {
        initApplicationContext();
    }

    public static DubboContext getInstance() {
        return instance;
    }

    private void initApplicationContext() {
        try {
            context = new ClassPathXmlApplicationContext("classpath:/dubbo-client.xml");
        } catch (BeansException e) {
            throw new IllegalArgumentException("Load dubbo-client.xml fail");
        }
    }

    public Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    public <T> T getBean(Class<T> clazz) {
        return (T) context.getBean(clazz);
    }
}
