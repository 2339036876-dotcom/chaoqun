package com.computer.controller;

import cn.hutool.core.util.StrUtil;
import com.computer.common.AuthSupport;
import com.computer.common.Result;
import com.computer.entity.Cart;
import com.computer.entity.Goods;
import com.computer.service.CartService;
import com.computer.service.GoodsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Resource
    private CartService cartService;
    @Resource
    private GoodsService goodsService;

    @GetMapping("/list")
    public Result list(HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        List<Cart> carts = cartService.lambdaQuery().eq(Cart::getUserId, userId).list();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Cart cart : carts) {
            Map<String, Object> item = new HashMap<>();
            item.put("cart", cart);
            item.put("goods", goodsService.getById(cart.getGoodsId()));
            result.add(item);
        }
        return Result.success(result);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Cart cart, HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        if (cart.getGoodsId() == null) {
            return Result.error("商品ID不能为空");
        }
        cart.setUserId(userId);
        Goods goods = goodsService.getById(cart.getGoodsId());
        if (goods == null) {
            return Result.error("商品不存在");
        }
        Cart exist = cartService.lambdaQuery()
                .eq(Cart::getUserId, userId)
                .eq(Cart::getGoodsId, cart.getGoodsId())
                .one();
        int addNum = cart.getNum() == null || cart.getNum() <= 0 ? 1 : cart.getNum();
        int targetNum = addNum + (exist == null ? 0 : exist.getNum());
        if (goods.getInventory() == null || goods.getInventory() < targetNum) {
            return Result.error("库存不足：" + StrUtil.blankToDefault(goods.getTitle(), "商品"));
        }
        if (exist != null) {
            exist.setNum(targetNum);
            cartService.updateById(exist);
        } else {
            cart.setNum(addNum);
            cartService.save(cart);
        }
        return Result.success("添加成功", null);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Cart cart, HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        Cart db = cartService.getById(cart.getCartId());
        if (db == null || !userId.equals(db.getUserId())) {
            return Result.error(403, "无权操作购物车");
        }
        if (cart.getNum() != null && cart.getNum() <= 0) {
            return Result.error("数量必须大于0");
        }
        if (cart.getNum() != null) {
            Goods goods = goodsService.getById(db.getGoodsId());
            if (goods == null) {
                return Result.error("商品不存在");
            }
            if (goods.getInventory() == null || goods.getInventory() < cart.getNum()) {
                return Result.error("库存不足：" + StrUtil.blankToDefault(goods.getTitle(), "商品"));
            }
        }
        cart.setUserId(userId);
        cartService.updateById(cart);
        return Result.success("修改成功", null);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        Cart db = cartService.getById(id);
        if (db == null || !userId.equals(db.getUserId())) {
            return Result.error(403, "无权操作购物车");
        }
        cartService.removeById(id);
        return Result.success("删除成功", null);
    }

    @DeleteMapping("/clear")
    public Result clear(HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        cartService.lambdaUpdate().eq(Cart::getUserId, userId).remove();
        return Result.success("清空成功", null);
    }
}
