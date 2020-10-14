package com.edu.portal.service;

import com.edu.bean.TbItemDesc;
import com.edu.portal.bean.ItemCustomer;

public interface ItemService {
    ItemCustomer getInfo(long itemId);

    TbItemDesc getDesc(long itemId);

    String getParam(long itemId);
}
