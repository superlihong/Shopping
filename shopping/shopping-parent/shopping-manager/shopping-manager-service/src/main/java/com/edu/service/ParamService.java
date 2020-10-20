package com.edu.service;

import com.edu.common.bean.EUDatagridResult;
import com.edu.common.bean.Message;
import com.edu.common.bean.ShoppingResult;

public interface ParamService {
    EUDatagridResult getAll(int page, int rows);

    ShoppingResult getitemparamcategoryId(Long categoryId);

    ShoppingResult insertitemparamcategoryId(Long categoryId, String paramData);
}
