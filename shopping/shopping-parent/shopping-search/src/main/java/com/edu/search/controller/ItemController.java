package com.edu.search.controller;

import com.edu.common.bean.ShoppingResult;
import com.edu.search.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;
    @ResponseBody
    @RequestMapping("import")
    public ShoppingResult imports(){
        try{
            itemService.save();
            return ShoppingResult.ok();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ShoppingResult.build(500,"错误");
    }
}
