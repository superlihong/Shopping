package com.edu.portal.interceptor;

import com.edu.bean.TbUser;
import com.edu.common.bean.CookieUtils;
import com.edu.common.bean.HttpClientUtil;
import com.edu.common.bean.ShoppingResult;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 拦截方法前再返回modelandview之前操作
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token= CookieUtils.getCookieValue(request,"TT_TOKEN",true);
        if(!StringUtils.isEmpty(token)){
            TbUser user=null;
            String tokenString=HttpClientUtil.doGet("http://localhost:8084/user/token/"+token);
            ShoppingResult result=ShoppingResult.formatToPojo(tokenString, TbUser.class);
            if(result.getStatus()==200){
                user= (TbUser) result.getData();
            }
            if(null==user){
                response.sendRedirect("http://localhost:8084/user/showLogin?redirect="+request.getRequestURL());
                return false;
            }
            return true;
        }else {
            response.sendRedirect("http://localhost:8084/user/showLogin?redirect="+request.getRequestURL());
            return false;
        }
    }
    /**
     * 拦截方法过程再返回modelandview之前操作
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
    /**
     * 拦截方法后再返回modelandview之后操作
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
