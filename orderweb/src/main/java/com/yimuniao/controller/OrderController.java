package com.yimuniao.controller;

import java.security.Principal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yimuniao.StepDefEnum;
import com.yimuniao.entity.OrderEntity;
import com.yimuniao.producer.ProducerService;
import com.yimuniao.service.OrderService;

@RestController
public class OrderController {

    @Value("${application.message:Hello World}")
    private String message = "Hello World";

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProducerService producerService;

    /**
     * handle the request that query the order by the orderid
     * 
     * @param request
     * @param response
     * @param id
     *            the ID of the order
     * @return
     */
    @RequestMapping(value = "/orders/{id}", method = RequestMethod.GET)
    @ResponseBody
    List<OrderEntity> welcome(HttpServletRequest request, HttpServletResponse response, @PathVariable int id,
            Principal principal) {
        final String userId = principal.getName();
        return orderService.get(id, userId);
    }

    private OrderEntity contructOrderEntity(Map<String, Object> payload, String userId) {
//        int id = Integer.parseInt((String) payload.get("orderId"));
        String description = (String) payload.get("description");
//        String startTime = (String) payload.get("startTime");
        OrderEntity Order = new OrderEntity();
//        Order.setOrderId(id);
        Order.setUserId(userId);
        Order.setDescription(description);
        Order.setStep(StepDefEnum.NEW.getStep());
//        Order.setStartTime(Date.valueOf(startTime));
        Order.setStartTime(new Date(System.currentTimeMillis()));
        return Order;
    }

    /**
     * 
     * @param request
     * @param response
     * @param payload
     * @param principal
     * @return
     */
    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    Map<String, Object> save(HttpServletRequest request, HttpServletResponse response,
            @RequestBody Map<String, Object> payload, Principal principal) {
        final String userId = principal.getName();
        OrderEntity Order = contructOrderEntity(payload, userId);
        OrderEntity savedOrder = orderService.save(Order);
        producerService.put(savedOrder);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        map.put("data", savedOrder);
        return map;
    }

    /**
     * 
     * @param request
     * @param response
     * @param id
     * @param model
     * @param payload
     * @param principal
     * @return
     */
    @RequestMapping(value = "/orders/{id}", method = RequestMethod.PUT)
    Map<String, Object> edit(HttpServletRequest request, HttpServletResponse response, @PathVariable int id,
            Model model, @RequestBody Map<String, Object> payload, Principal principal) {
        final String userId = principal.getName();
        OrderEntity Order = contructOrderEntity(payload, userId);

        orderService.update(Order);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        map.put("data", Order);
        return map;
    }

    /**
     * 
     * @param request
     * @param response
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/orders/{id}", method = RequestMethod.DELETE)
    boolean delete(HttpServletRequest request, HttpServletResponse response, @PathVariable int id, Model model) {
        orderService.delete(id);
        model.addAttribute("success", true);
        return true;
    }

}