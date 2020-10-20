package com.edu.service.impl;

import com.edu.bean.TbItem;
import com.edu.bean.TbItemParam;
import com.edu.bean.TbItemParamExample;
import com.edu.common.bean.EUDatagridResult;
import com.edu.common.bean.Message;
import com.edu.common.bean.ShoppingResult;
import com.edu.mapper.TbItemParamMapper;
import com.edu.service.ParamService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ParamServiceImpl implements ParamService {
    @Autowired
    private TbItemParamMapper tbItemParamMapper;

    @Override
    public EUDatagridResult getAll(int page, int rows) {
        EUDatagridResult euDatagridResult=new EUDatagridResult();
        PageHelper.startPage(page,rows);
        List<TbItemParam> tbItemList=tbItemParamMapper.selectByExampleWithBLOBs(new TbItemParamExample());
        PageInfo pageInfo=new PageInfo(tbItemList);
        euDatagridResult.setTotal(pageInfo.getTotal());
        euDatagridResult.setRows(pageInfo.getList());
        return euDatagridResult;
    }

    @Override
    public ShoppingResult getitemparamcategoryId(Long categoryId) {
        TbItemParamExample example=new TbItemParamExample();
        example.createCriteria().andItemCatIdEqualTo(categoryId);
        List<TbItemParam> tbItemList=tbItemParamMapper.selectByExampleWithBLOBs(example);
        if(tbItemList!=null &&tbItemList.size()>0){
            return ShoppingResult.ok(tbItemList.get(0));
        }
        return ShoppingResult.build(500,"商品不存在!");
    }

    @Override
    public ShoppingResult insertitemparamcategoryId(Long categoryId, String paramData) {
        TbItemParam itemParam=new TbItemParam();
        itemParam.setItemCatId(categoryId);
        itemParam.setParamData(paramData);
        itemParam.setCreated(new Date());
        itemParam.setUpdated(new Date());
        tbItemParamMapper.insertSelective(itemParam);
        return ShoppingResult.ok();
    }
}
