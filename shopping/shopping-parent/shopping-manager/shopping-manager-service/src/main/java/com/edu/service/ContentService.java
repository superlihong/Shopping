package com.edu.service;

import com.edu.bean.TbContent;
import com.edu.common.bean.EUDatagridResult;
import com.edu.common.bean.ShoppingResult;

import java.util.List;

public interface ContentService {
    EUDatagridResult getAllbycategoryId(Long categoryId,int page ,int rows);

    ShoppingResult insertContent(TbContent content);
}
