package com.edu.service.impl;

import com.edu.bean.TbContentCategory;
import com.edu.bean.TbContentCategoryExample;
import com.edu.common.bean.EUTreeResult;
import com.edu.common.bean.ShoppingResult;
import com.edu.mapper.TbContentCategoryMapper;
import com.edu.service.ContentCategoryService;
import javafx.scene.text.TextBoundsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;
    @Override
    public List<EUTreeResult> getAll(Long id) {
        TbContentCategoryExample example=new TbContentCategoryExample();
        example.createCriteria().andParentIdEqualTo(id);
        List<TbContentCategory> contentCategories=contentCategoryMapper.selectByExample(example);
        List<EUTreeResult>  treeResults=new ArrayList<>();
        for (TbContentCategory contentCategor:contentCategories){
            EUTreeResult euTreeResult=new EUTreeResult();
            euTreeResult.setState(contentCategor.getIsParent()?"closed":"open");
            euTreeResult.setId(contentCategor.getId());
            euTreeResult.setText(contentCategor.getName());
            treeResults.add(euTreeResult);
        }
        return treeResults;
    }

    @Override
    public ShoppingResult insertcontentCategory(Long parentId, String name) {
        TbContentCategory tbContentCategory=new TbContentCategory();
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setName(name);
        tbContentCategory.setStatus(1);
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setIsParent(false);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        contentCategoryMapper.insertSelective(tbContentCategory);
        TbContentCategory parent=contentCategoryMapper.selectByPrimaryKey(parentId);
        if(!parent.getIsParent()){
            parent.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKeySelective(parent);
        }
        return ShoppingResult.ok(tbContentCategory);
    }

    @Override
    public ShoppingResult updatecontentCategory(Long id, String name) {
       TbContentCategory contentCategory
        =contentCategoryMapper.selectByPrimaryKey(id);
        contentCategory.setName(name);
        contentCategory.setUpdated(new Date());
        contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
        return ShoppingResult.ok();
    }

    @Override
    public ShoppingResult deletecontentCategory(Long id) {
        TbContentCategory tbContentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        if (tbContentCategory.getIsParent()){
            caseCodeAll(id);
            deletecontentCategory(id);
        }else {
            contentCategoryMapper.deleteByPrimaryKey(id);
            updateCodeAll(tbContentCategory.getParentId());
        }
        return ShoppingResult.ok();
    }

    private void caseCodeAll(long id) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(id);
        List<TbContentCategory> tbContentCategory = contentCategoryMapper.selectByExample(example);
        for (TbContentCategory category : tbContentCategory){
            if (category.getIsParent()){
                caseCodeAll(category.getId());
            }else {
                contentCategoryMapper.deleteByPrimaryKey(category.getId());
                updateCodeAll(category.getParentId());
            }
        }
    }

    //判断父节点下面是否还有子节点，如果没有修改父节点的isParent为false
    private void updateCodeAll(long id) {
        TbContentCategory tbContentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(tbContentCategory.getId());
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        if (list.size()<=0){
            tbContentCategory.setIsParent(false);
            tbContentCategory.setUpdated(new Date());
            contentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
        }
    }
}
