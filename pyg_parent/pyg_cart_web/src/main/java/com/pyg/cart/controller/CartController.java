package com.pyg.cart.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pyg.cart.service.CartService;
import com.pyg.pojogroup.Cart;
import com.pyg.utils.CookieUtil;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Reference(timeout = 6000)
    private CartService cartService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    /**
     * 购物车列表
     */
    @RequestMapping("/findCartList")
    public List<Cart> findCartList(){
        //当前登录人账号
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前登录人：" + username);

        String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
        if (cartListString==null || cartListString.equals("")){
            cartListString="[]";
        }
        List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);
        //未登录
        if (username.equals("anonymousUser")){
            System.out.println("从cookie中提取购物车");
            return cartList_cookie;
        }else {
            //已登录，从redis中获取购物车
            List<Cart> cartList_redis = cartService.findCartListFromRedis(username);
            if (cartList_cookie.size()>0){
                List<Cart> cartList = cartService.mergeCartList(cartList_cookie, cartList_redis);
                cartService.saveCartListToRedis(username, cartList);
                //本地购物车清除
                CookieUtil.deleteCookie(request, response, "cartList");

                System.out.println("执行了合并购物车的逻辑");
                return cartList;
            }
            return cartList_redis;
        }
    }

    /**
     * 添加商品到购物车
     */
    @RequestMapping("/addGoodsToCartList")
    @CrossOrigin(origins="http://localhost:9105",allowCredentials="true")
    public Result addGoodsToCartList(Long itemId,Integer num){
/*        response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105");
        response.setHeader("Access-Control-Allow-Credentials", "true");*/

        //当前登录人账号
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前登录人：" + name);

        try {
            List<Cart> cartList = findCartList();
            cartList = cartService.addGoodsToCartList(cartList,itemId,num);
            if (name.equals("anonymousUser")) {//如果未登录
                //将新的购物车存入cookie
                String cartListString = JSON.toJSONString(cartList);
                CookieUtil.setCookie(request, response, "cartList", cartListString, 3600 * 24, "UTF-8");
                System.out.println("向cookie存储购物车");

            } else {//如果登录
                cartService.saveCartListToRedis(name, cartList);
            }
            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }
}
