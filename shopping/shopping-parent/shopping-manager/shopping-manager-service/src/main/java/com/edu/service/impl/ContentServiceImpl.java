package com.edu.service.impl;

import com.edu.bean.TbContent;
import com.edu.bean.TbContentExample;
import com.edu.common.bean.EUDatagridResult;
import com.edu.common.bean.EUTreeResult;
import com.edu.common.bean.HttpClientUtil;
import com.edu.common.bean.ShoppingResult;
import com.edu.mapper.TbContentMapper;
import com.edu.service.ContentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper tbContentMapper;
    @Value("${REMOTE_SERVICE_BASE}")
    private String REMOTE_SERVICE_BASE;
    @Value("${REDIS_CACHE_BID_PIC}")
    private String REDIS_CACHE_BID_PIC;
    @Override
    public EUDatagridResult getAllbycategoryId(Long categoryId,int page ,int rows) {
        PageHelper.startPage(page,rows);

        TbContentExample example=new TbContentExample();
        example.createCriteria().andCategoryIdEqualTo(categoryId);
        List<TbContent> contentList=tbContentMapper.selectByExample(example);
        PageInfo pageInfo=new PageInfo(contentList);
       EUDatagridResult euDatagridResult=new EUDatagridResult();
       euDatagridResult.setTotal(pageInfo.getTotal());
       euDatagridResult.setRows(pageInfo.getList());

        return euDatagridResult;
    }

    @Override
    public ShoppingResult insertContent(TbContent content) {
        content.setCreated(new Date());
        content.setUpdated(new Date());
        tbContentMapper.insertSelective(content);
        //维护缓存
        HttpClientUtil.doGet(REMOTE_SERVICE_BASE+REDIS_CACHE_BID_PIC+"/"+content.getCategoryId()+"");
        return ShoppingResult.ok();
    }
}
