package com.edu.service.impl;

import com.edu.bean.TbItem;
import com.edu.bean.TbItemDesc;
import com.edu.bean.TbItemParamItem;
import com.edu.bean.TbItemParamItemExample;
import com.edu.common.bean.EUDatagridResult;
import com.edu.common.bean.IDUtils;
import com.edu.common.bean.ShoppingResult;
import com.edu.mapper.TbItemDescMapper;
import com.edu.mapper.TbItemMapper;
import com.edu.mapper.TbItemParamItemMapper;
import com.edu.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;
    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;

    @Override
    public EUDatagridResult getAll(int page, int rows) {
        EUDatagridResult euDatagridResult=new EUDatagridResult();
        PageHelper.startPage(page,rows);
        List<TbItem> tbItemList=tbItemMapper.selectByExample(null);
        PageInfo pageInfo=new PageInfo(tbItemList);
        euDatagridResult.setTotal(pageInfo.getTotal());
        euDatagridResult.setRows(pageInfo.getList());
        return euDatagridResult;
    }

    @Override
    public ShoppingResult insertItem(TbItem item, String desc, String itemParams) {
        long itemId=IDUtils.genItemId();
        item.setId(itemId);
        item.setStatus((byte)1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        tbItemMapper.insertSelective(item);
        //保存商品所对应的描述和对应参数
        ShoppingResult result=insertItemDesc(itemId,desc);
        if(result.getStatus()==200){
            result =inserTbItemParamitem(itemId,itemParams);
            if (result.getStatus()==200){
                return ShoppingResult.ok();
            }else {
                throw new RuntimeException();
            }
        }else {
            //它抛异常，在管理事务中spring就会自动回滚
            throw new RuntimeException();
        }

    }

    private ShoppingResult inserTbItemParamitem(long itemId, String itemParams) {
        TbItemParamItem itemParamItem=new TbItemParamItem();
        itemParamItem.setItemId(itemId);
        itemParamItem.setParamData(itemParams);
        itemParamItem.setCreated(new Date());
        itemParamItem.setUpdated(new Date());
        itemParamItemMapper.insertSelective(itemParamItem);
        return ShoppingResult.ok();
    }

    private ShoppingResult insertItemDesc(long itemId, String desc) {
        TbItemDesc itemDesc=new TbItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        itemDescMapper.insertSelective(itemDesc);
        return ShoppingResult.ok();
    }
}
