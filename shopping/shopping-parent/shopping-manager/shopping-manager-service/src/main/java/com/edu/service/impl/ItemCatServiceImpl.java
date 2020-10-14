package com.edu.service.impl;

import com.edu.bean.TbItemCat;
import com.edu.bean.TbItemCatExample;
import com.edu.common.bean.EUTreeResult;
import com.edu.mapper.TbItemCatMapper;
import com.edu.service.ItemCatService;
import com.edu.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<EUTreeResult> getAll(Long id) {
        List<EUTreeResult> treeResults=new ArrayList<>();
        TbItemCatExample tbItemCatExample=new TbItemCatExample();
        tbItemCatExample.createCriteria().andParentIdEqualTo(id);
        List<TbItemCat> tbItemCatList=tbItemCatMapper.selectByExample(tbItemCatExample);
        for (TbItemCat itemCat:tbItemCatList){
            EUTreeResult euTreeResult=new EUTreeResult();
            euTreeResult.setId(itemCat.getId());
            euTreeResult.setState(itemCat.getIsParent()?"closed":"open");
            euTreeResult.setText(itemCat.getName());
            treeResults.add(euTreeResult);
        }
        return treeResults;
    }
}
