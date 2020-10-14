package com.edu.service;

import com.edu.common.bean.EUTreeResult;
import com.edu.common.bean.ShoppingResult;

import java.util.List;

public interface ContentCategoryService {
    List<EUTreeResult> getAll(Long id);
    ShoppingResult insertcontentCategory(Long parentId, String name);

    ShoppingResult updatecontentCategory(Long id, String name);

    ShoppingResult deletecontentCategory(Long id);
}
