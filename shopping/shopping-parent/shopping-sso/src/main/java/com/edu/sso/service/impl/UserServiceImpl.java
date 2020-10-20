package com.edu.sso.service.impl;

import com.edu.bean.TbUser;
import com.edu.bean.TbUserExample;
import com.edu.common.bean.CookieUtils;
import com.edu.common.bean.JsonUtils;
import com.edu.common.bean.ShoppingResult;
import com.edu.mapper.TbUserMapper;
import com.edu.sso.dao.RedisDao;
import com.edu.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private RedisDao redisDao;
    @Value("${REDIS_TOKEN}")
    private String REDIS_TOKEN;
    @Value("${REDIS_TOKEN_EXPIRE}")
    private int REDIS_TOKEN_EXPIRE;
    @Override
    public ShoppingResult getUserByNameAndType(String param, int type) {
        TbUserExample example=new TbUserExample();
        TbUserExample.Criteria criteria=example.createCriteria();
        if(1==type){
            criteria.andUsernameEqualTo(param);
        }else if (2==type){
            criteria.andPhoneEqualTo(param);
        }
        else if (3==type){
            criteria.andEmailEqualTo(param);
        }
        List<TbUser> userList=tbUserMapper.selectByExample(example);
        if (null!=userList &&userList.size()>0){
            return ShoppingResult.ok(false);
        }
        return ShoppingResult.ok(true);
    }

    @Override
    public ShoppingResult saveUser(TbUser tbUser) {
        try{
            tbUser.setCreated(new Date());
            tbUser.setUpdated(new Date());
            tbUser.setPassword(DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes()));
            tbUserMapper.insertSelective(tbUser);
            return ShoppingResult.ok();
        }catch (Exception e){
            e.printStackTrace();
            return ShoppingResult.build(400,"注册失败. 请校验数据后请再提交数据.");
        }

    }

    @Override
    public ShoppingResult login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        TbUserExample example=new TbUserExample();
        TbUserExample.Criteria criteria=example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> userList=tbUserMapper.selectByExample(example);
        if(null==userList || userList.size()==0){
            ShoppingResult.build(400,"用户名或者密码错误");
        }
        TbUser user=userList.get(0);
        if(!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))){
            ShoppingResult.build(400,"用户名或者密码错误");
        }
        String token= UUID.randomUUID().toString();
        //放入缓存
        redisDao.set(REDIS_TOKEN+":"+token,JsonUtils.objectToJson(user));
        //过期时间
       redisDao.expire(REDIS_TOKEN+":"+token,REDIS_TOKEN_EXPIRE);
       //token放入浏览器的cookie
        CookieUtils.setCookie(request,response,"TT_TOKEN",token,true);
        return ShoppingResult.ok(token);
    }

    @Override
    public TbUser token(String token, String callback) {
        String result=redisDao.get(REDIS_TOKEN+":"+token);
        if(StringUtils.isEmpty(result)){
            return null;
        }else {
            TbUser user=JsonUtils.jsonToPojo(result,TbUser.class);
            //从新设置token的过期时间
            redisDao.expire(REDIS_TOKEN+":"+token,REDIS_TOKEN_EXPIRE);
            return user;
        }

    }

    @Override
    public ShoppingResult logout(String token) {
        redisDao.expire(REDIS_TOKEN+":"+token,0);
        return ShoppingResult.ok();
    }
}
