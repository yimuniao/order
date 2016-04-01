package com.yimuniao.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yimuniao.config.SampleProperties;

@RestController
public class ExampleController {

    @RequestMapping("/indexx")
    String home(HttpServletRequest request, HttpServletResponse response) {
        return "Hello World!";
    }

    @Autowired
    private SampleProperties properties;

    // @RequestMapping(value="/{user}", method=RequestMethod.GET)
    // public User getUser(@PathVariable Long user) {
    // // ...
    // }
    //
    // @RequestMapping(value="/{user}/customers", method=RequestMethod.GET)
    // List<Customer> getUserCustomers(@PathVariable Long user) {
    // // ...
    // }
    //
    // @RequestMapping(value="/{user}", method=RequestMethod.DELETE)
    // public User deleteUser(@PathVariable Long user) {
    // // ...
    // }

}