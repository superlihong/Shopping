package com.edu.portal.service.impl;

import com.edu.common.bean.HttpClientUtil;
import com.edu.common.bean.SearchResult;
import com.edu.common.bean.ShoppingResult;
import com.edu.portal.service.SearchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {
    @Value("${SOLR_BASE_URL}")
    private String SOLR_BASE_URL;
    @Override
    public SearchResult query(String q, int page) {
        Map<String,String> params=new HashMap<>();
        params.put("q",q);
        params.put("page",page+"");
        String result=HttpClientUtil.doGet(SOLR_BASE_URL,params);
        //ShoppingResult.formatToPojo(result,SearchResult.class);
        //把result字符串数据转为SearchResult对象
        //shoppingResult.getData();把数据转为data类型

        ShoppingResult shoppingResult=ShoppingResult.formatToPojo(result,SearchResult.class);
        if(shoppingResult.getStatus()==200){
            return (SearchResult) shoppingResult.getData();
        }
        return null;
    }
}
