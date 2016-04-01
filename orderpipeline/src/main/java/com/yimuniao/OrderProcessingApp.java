package com.yimuniao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yimuniao.scheduler.SchedulerService;

public class OrderProcessingApp {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        SchedulerService schedulerService = (SchedulerService) context.getBean("scheduler");
        schedulerService.start();
    }
}
