package com.edu.rest.service.impl;

import com.edu.common.bean.ShoppingResult;
import com.edu.rest.dao.RedisDao;
import com.edu.rest.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImp implements CacheService {
    @Autowired
    private RedisDao redisDao;
    @Value("${INDEX_CACHE_CONTENT_PIC}")
    private String INDEX_CACHE_CONTENT_PIC;

    @Override
    public ShoppingResult syncContentCatgoryCache(long categoryId) {
        redisDao.hdel(INDEX_CACHE_CONTENT_PIC,categoryId+"");
        return ShoppingResult.ok();
    }
}
