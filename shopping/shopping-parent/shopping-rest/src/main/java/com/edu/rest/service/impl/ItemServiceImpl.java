package com.edu.rest.service.impl;

import com.edu.bean.*;
import com.edu.common.bean.JsonUtils;
import com.edu.common.bean.ShoppingResult;
import com.edu.mapper.TbItemDescMapper;
import com.edu.mapper.TbItemMapper;
import com.edu.mapper.TbItemParamItemMapper;
import com.edu.rest.dao.RedisDao;
import com.edu.rest.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private RedisDao redisDao;
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;
    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;
    @Value("${REDIS_ITEM_KEY}")
    private String REDIS_ITEM_KEY;
    @Value("${REDIS_ITEM_EXPIRE}")
    private int REDIS_ITEM_EXPIRE;
    @Override
    public ShoppingResult getInfo(long itemId) {
        TbItem tbItem=null;
        try{
            //用可以取
            String infoItem=redisDao.get(REDIS_ITEM_KEY+":"+itemId+":INFO");
            if(!StringUtils.isEmpty(infoItem)){
                //把json格式的字符串转化Tbitem对象
                tbItem= JsonUtils.jsonToPojo(infoItem, TbItem.class);
                return ShoppingResult.ok(tbItem);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //没有从数据库拿
        tbItem=itemMapper.selectByPrimaryKey(itemId);
        try {
            //放入缓存
            redisDao.set(REDIS_ITEM_KEY+":"+itemId+":INFO",JsonUtils.objectToJson(tbItem));
            redisDao.expire(REDIS_ITEM_KEY+":"+itemId+":INFO",REDIS_ITEM_EXPIRE);
            return ShoppingResult.ok(tbItem);
        }catch (Exception e){
            e.printStackTrace();
            return ShoppingResult.build(500,"出错了！");
        }

    }

    @Override
    public ShoppingResult getDesc(long itemId) {
        TbItemDesc tbItemDesc=null;
        try{
            //用可以取
            String infoItem=redisDao.get(REDIS_ITEM_KEY+":"+itemId+":DESC");
            if(!StringUtils.isEmpty(infoItem)){
                //把json格式的字符串转化Tbitem对象
                tbItemDesc= JsonUtils.jsonToPojo(infoItem, TbItemDesc.class);
                return ShoppingResult.ok(tbItemDesc);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //没有从数据库拿
        tbItemDesc=itemDescMapper.selectByPrimaryKey(itemId);
        try {
            //放入缓存
            redisDao.set(REDIS_ITEM_KEY+":"+itemId+":DESC",JsonUtils.objectToJson(tbItemDesc));
            redisDao.expire(REDIS_ITEM_KEY+":"+itemId+":DESC",REDIS_ITEM_EXPIRE);
            return ShoppingResult.ok(tbItemDesc);
        }catch (Exception e){
            e.printStackTrace();
            return ShoppingResult.build(500,"出错了！");
        }
    }

    @Override
    public ShoppingResult getParam(long itemId) {
        TbItemParamItem tbItemParamitem=null;
        try{
            //用可以取
            String infoItem=redisDao.get(REDIS_ITEM_KEY+":"+itemId+":PARAM");
            if(!StringUtils.isEmpty(infoItem)){
                //把json格式的字符串转化Tbitem对象
                tbItemParamitem= JsonUtils.jsonToPojo(infoItem, TbItemParamItem.class);
                return ShoppingResult.ok(tbItemParamitem);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //没有从数据库拿
        TbItemParamItemExample example=new TbItemParamItemExample();
        example.createCriteria().andItemIdEqualTo(itemId);
        List<TbItemParamItem> tbItemParamlist=itemParamItemMapper.selectByExampleWithBLOBs(example);

        try {
            //放入缓存
            if (null !=tbItemParamlist && tbItemParamlist.size()>0){
                tbItemParamitem=tbItemParamlist.get(0);
                redisDao.set(REDIS_ITEM_KEY+":"+itemId+":PARAM",JsonUtils.objectToJson(tbItemParamitem));
                redisDao.expire(REDIS_ITEM_KEY+":"+itemId+":PARAM",REDIS_ITEM_EXPIRE);
                return ShoppingResult.ok(tbItemParamitem);
            }

        }catch (Exception e){
            e.printStackTrace();
            return ShoppingResult.build(500,"出错了！");
        }
        return ShoppingResult.build(500,"出错了！");
    }


}
