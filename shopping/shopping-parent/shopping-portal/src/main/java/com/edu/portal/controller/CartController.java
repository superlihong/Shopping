package com.edu.portal.controller;

import com.edu.portal.bean.CartItem;
import com.edu.portal.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @RequestMapping("/add/{itemId}")
    public String add(@PathVariable long itemId, @RequestParam(value = "num",defaultValue = "1") int num, Model model, HttpServletRequest request, HttpServletResponse response){
        List<CartItem> list=cartService.addCartItem(itemId,num,request,response);
        model.addAttribute("cartList",list);
        return "cart";
    }
    @RequestMapping("/list")
    public String list(Model model, HttpServletRequest request, HttpServletResponse response){
        List<CartItem> list=cartService.getAll(request,response);
        model.addAttribute("cartList",list);
        return "cart";
    }
    @RequestMapping("/delete/{cartId}")
    public String del(@PathVariable long cartId, HttpServletRequest request, HttpServletResponse response){
        cartService.delCartItem(cartId,request,response);
        return "redirect:/cart/list.html";
    }

}
