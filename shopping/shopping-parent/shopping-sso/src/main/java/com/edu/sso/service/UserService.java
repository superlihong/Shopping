package com.edu.sso.service;

import com.edu.bean.TbUser;
import com.edu.common.bean.ShoppingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {
    ShoppingResult getUserByNameAndType(String param, int type);

    ShoppingResult saveUser(TbUser tbUser);

    ShoppingResult login(String username, String password, HttpServletRequest request, HttpServletResponse response);


    TbUser token(String token, String callback);

    ShoppingResult logout(String token);
}
