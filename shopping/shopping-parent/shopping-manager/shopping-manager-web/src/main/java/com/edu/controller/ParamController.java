package com.edu.controller;

import com.edu.bean.TbItem;
import com.edu.common.bean.EUDatagridResult;
import com.edu.common.bean.Message;
import com.edu.common.bean.ShoppingResult;
import com.edu.service.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//规格分组
@Controller
@RequestMapping("/item/param")
public class ParamController {
    @Autowired
    private ParamService paramService;
    @ResponseBody
    @RequestMapping("/list")
    public EUDatagridResult list(int page,int rows){
        return paramService.getAll(page,rows);
    }
    @ResponseBody
    @RequestMapping("/query/itemcatid/{categoryId}")
    public ShoppingResult itemParam(@PathVariable("categoryId")  Long categoryId){
        return paramService.getitemparamcategoryId(categoryId);
    }
    @ResponseBody
    @RequestMapping("/save/{categoryId}")
    public ShoppingResult save(@PathVariable("categoryId")  Long categoryId, String paramData){
        return paramService.insertitemparamcategoryId(categoryId,paramData);
    }
}
