package com.edu.portal.service.impl;

import com.edu.common.bean.HttpClientUtil;
import com.edu.common.bean.JsonUtils;
import com.edu.common.bean.ShoppingResult;
import com.edu.portal.bean.Order;
import com.edu.portal.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Value("${ORDER_BASE_URL}")
    private String ORDER_BASE_URL;
    @Override
    public ShoppingResult create(Order order) {
        String str=HttpClientUtil.doPostJson(ORDER_BASE_URL, JsonUtils.objectToJson(order));

        return ShoppingResult.format(str);
    }
}
