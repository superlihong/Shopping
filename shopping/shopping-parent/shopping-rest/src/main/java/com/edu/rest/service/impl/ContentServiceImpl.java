package com.edu.rest.service.impl;

import com.edu.bean.TbContent;
import com.edu.bean.TbContentExample;
import com.edu.common.bean.JsonUtils;
import com.edu.mapper.TbContentMapper;
import com.edu.rest.dao.RedisDao;
import com.edu.rest.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper tbContentMapper;
    @Value("${INDEX_CACHE_CONTENT_PIC}")
    private String INDEX_CACHE_CONTENT_PIC;
    @Autowired
    private RedisDao redisDao;
    @Override
    public List<Map<String, Object>> getAllbycategoryId(Long categoryId) {
        List<TbContent> tbContentList=null;
        //先从缓存中取
        try {
            String str = redisDao.hget(INDEX_CACHE_CONTENT_PIC,categoryId+"");
            if(!StringUtils.isEmpty(str)) {
                tbContentList = JsonUtils.jsonToList(str,TbContent.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        List<Map<String,Object>> list=new ArrayList<>();
        if(tbContentList==null){
            TbContentExample example=new TbContentExample();
            example.createCriteria().andCategoryIdEqualTo(categoryId);
            tbContentList=tbContentMapper.selectByExample(example);
            //放入缓存
            try {
                String result = JsonUtils.objectToJson(tbContentList);
                redisDao.hset(INDEX_CACHE_CONTENT_PIC,categoryId+"",result);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

       for (TbContent content:tbContentList){
           Map<String ,Object> map=new HashMap<>();
           map.put("src",content.getPic());
           map.put("height",240);
           map.put("width",670);
           map.put("alt",content.getSubTitle());
           map.put("srcB",content.getPic2());
           map.put("heightB",240);
           map.put("widthB",550);
           map.put("href",content.getUrl());
           list.add(map);
       }
        return list;
    }
}
