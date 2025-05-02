package com.medvedev.backend;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class BeanLogger implements ApplicationRunner {

    private final ApplicationContext applicationContext;

    public BeanLogger(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) {
        String[] beans = applicationContext.getBeanDefinitionNames();
        Arrays.stream(beans)
                .filter(bean -> bean.startsWith("com.medvedev"))
                .sorted()
                .forEach(System.out::println);
    }

}
