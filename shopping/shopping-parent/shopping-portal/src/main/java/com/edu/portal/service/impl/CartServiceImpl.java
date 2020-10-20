package com.edu.portal.service.impl;

import com.edu.bean.TbItem;
import com.edu.common.bean.CookieUtils;
import com.edu.common.bean.HttpClientUtil;
import com.edu.common.bean.JsonUtils;
import com.edu.common.bean.ShoppingResult;
import com.edu.portal.bean.CartItem;
import com.edu.portal.service.CartService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
@Service
public class CartServiceImpl implements CartService {
    @Value("${BASE_URL}")
    private String BASE_URL;
    @Value("${ITEM_BASE_URL}")
    private String ITEM_BASE_URL;
    @Override
    public List<CartItem> addCartItem(long itemId, int num, HttpServletRequest request, HttpServletResponse response) {
        //查询所有的缓存数据
        List<CartItem> cartItems=getAllCartItem(request);
        CartItem item=null;
        for (CartItem cartItem:cartItems){
            if (cartItem.getId()==itemId){
                cartItem.setNum(cartItem.getNum()+num);
                item=cartItem;
                break;
            }
        }
        if (null==item){
            item=new CartItem();
            //取出当前商品
            String tbItemString=HttpClientUtil.doGet(BASE_URL+ITEM_BASE_URL+itemId);
            ShoppingResult shoppingResult=ShoppingResult.formatToPojo(tbItemString, TbItem.class);
            if (shoppingResult.getStatus()==200){
                TbItem tbItem= (TbItem) shoppingResult.getData();
                item.setId(tbItem.getId());
                item.setTitle(tbItem.getTitle());
                item.setPrice(tbItem.getPrice());
                item.setNum(num);
                item.setImage(tbItem.getImage());
                cartItems.add(item);

            }
            CookieUtils.setCookie(request,response,"TT_CART",JsonUtils.objectToJson(cartItems),true);
        }
        return cartItems;
    }

    @Override
    public List<CartItem> getAll(HttpServletRequest request, HttpServletResponse response) {
        return getAllCartItem(request);
    }

    @Override
    public void delCartItem(long cartId, HttpServletRequest request, HttpServletResponse response) {
        List<CartItem> cartItems=getAllCartItem(request);
        //判断是否在缓存中
        for (CartItem cartItem:cartItems){
            if(cartItem.getId()==cartId){
                cartItems.remove(cartItem);
               break;
            }
        }
        CookieUtils.setCookie(request,response,"TT_CART",JsonUtils.objectToJson(cartItems),true);
    }

    /**
     * 查询所有的缓存数据
     * @return
     */
    private List<CartItem> getAllCartItem(HttpServletRequest request) {
        String cartString=CookieUtils.getCookieValue(request,"TT_CART",true);
        if(StringUtils.isEmpty(cartString)){
            //从cookie中那
            return new ArrayList<>();
        }
        //如果cookie中拿到了数据，要把这个字符串转换成购物车
        return JsonUtils.jsonToList(cartString,CartItem.class);
    }
}
