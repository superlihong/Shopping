package com.edu.order.controller;

import com.edu.common.bean.ShoppingResult;
import com.edu.order.bean.Order;
import com.edu.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    //把对象转换成json格式，然后响应 printwriter
    //RequestBody把json格式装换成对象
    @ResponseBody
    @RequestMapping(value = "/order/create",method = RequestMethod.POST)
    public ShoppingResult create(@RequestBody Order order){
        //调用order换回id
        String orderId=orderService.create(order);
        return ShoppingResult.ok(orderId);
    }
}
