package com.edu.rest.service.impl;

import com.edu.bean.TbItemCat;
import com.edu.bean.TbItemCatExample;
import com.edu.mapper.TbItemCatMapper;
import com.edu.rest.bean.CatNode;
import com.edu.rest.bean.CatResultustNode;
import com.edu.rest.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceimpl implements CategoryService {
    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Override
    public CatResultustNode getAll() {
        CatResultustNode catResultustNode = new CatResultustNode();
        catResultustNode.setData(getcatnode(0));
        return catResultustNode;
    }

    private List<?> getcatnode(long parentId) {
        TbItemCatExample example = new TbItemCatExample();
        example.createCriteria().andParentIdEqualTo(parentId);
        List<TbItemCat> itemCats = itemCatMapper.selectByExample(example);
        List results = new ArrayList();
        int count=0;
        for (TbItemCat itemCat : itemCats) {
            CatNode catNode = new CatNode();
            if (itemCat.getIsParent()) {
                if (parentId == 0) {
                    catNode.setName("<a href='/products/" + itemCat.getId() + ".html'>" + itemCat.getName()+"</a>");
                } else {
                    catNode.setName(itemCat.getName());
                }
                catNode.setUrl("/products/" + itemCat.getId() + ".html");
                catNode.setItem(getcatnode(itemCat.getId()));
                results.add(catNode);
                count ++;
                if(count>=14 & parentId==0){
                    break;
                }
            } else {
                results.add("/products/" + itemCat.getId() + ".html|" + itemCat.getName());
            }

        }
        return results;
    }
}
