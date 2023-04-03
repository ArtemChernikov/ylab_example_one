package ru.ylab.exampleone.chernikov.lesson05.messagefilter;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MessageFilterApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();
        Filter filter = applicationContext.getBean(FilterImpl.class);
        filter.processMessage();
        applicationContext.close();
    }
}
