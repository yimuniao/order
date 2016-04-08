package com.yimuniao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yimuniao.scanner.ScannerService;

public class OrderScannerApp {
    public static void main(String[] args)  throws Exception{
        System.out.println("Hello World!");
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        ScannerService schedulerService = (ScannerService) context.getBean("scheduler");
        schedulerService.start();
    }
}
