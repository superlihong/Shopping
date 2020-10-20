package com.edu.portal.controller;

import com.edu.bean.TbItemDesc;
import com.edu.portal.bean.ItemCustomer;
import com.edu.portal.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;
    @RequestMapping("/item/{itemId}")
    public String info(@PathVariable(value = "itemId") long itemId, Model model){
        ItemCustomer customer=itemService.getInfo(itemId);
        model.addAttribute("item",customer);
        return "item";
    }
    @ResponseBody
    @RequestMapping("/item/desc/{itemId}")
    public String desc(@PathVariable(value = "itemId") long itemId, Model model){
        TbItemDesc tbItemDesc=itemService.getDesc(itemId);
        return tbItemDesc.getItemDesc();
    }
    @ResponseBody
    @RequestMapping(value = "/item/param/{itemId}",produces = MediaType.TEXT_HTML_VALUE+";charset=UTF-8")
    public String paaram(@PathVariable(value = "itemId") long itemId){
        String param=itemService.getParam(itemId);
        return param;
    }
}
