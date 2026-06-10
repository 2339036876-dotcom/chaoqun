package com.computer.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.computer.entity.Cart;
import com.computer.entity.Goods;
import com.computer.entity.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderCheckoutService {

    @Resource
    private OrderService orderService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private CartService cartService;

    @Transactional(rollbackFor = Exception.class)
    public List<Order> checkoutCart(Integer userId, String contactName, String contactPhone,
                                    String contactAddress, String remark, Double vipDiscount) {
        List<Cart> carts = cartService.lambdaQuery().eq(Cart::getUserId, userId).list();
        if (carts.isEmpty()) {
            throw new IllegalStateException("购物车为空");
        }
        for (Cart c : carts) {
            Goods g = goodsService.getById(c.getGoodsId());
            if (g == null) {
                throw new IllegalStateException("商品不存在或已下架");
            }
            if (g.getInventory() == null || g.getInventory() < c.getNum()) {
                throw new IllegalStateException("库存不足：" + StrUtil.blankToDefault(g.getTitle(), "商品"));
            }
        }
        List<Order> created = new ArrayList<>();
        for (Cart c : carts) {
            Goods g = goodsService.getById(c.getGoodsId());
            if (g.getInventory() == null || g.getInventory() < c.getNum()) {
                throw new IllegalStateException("库存不足：" + StrUtil.blankToDefault(g.getTitle(), "商品"));
            }
            Order order = new Order();
            order.setUserId(userId);
            order.setOrderNumber(IdUtil.getSnowflakeNextIdStr());
            order.setGoodsId(g.getGoodsId());
            order.setNum(c.getNum());
            order.setState("待付款");
            order.setDeliveryState("未配送");
            order.setContactName(contactName);
            order.setContactPhone(contactPhone);
            order.setContactAddress(contactAddress);
            if (StrUtil.isNotBlank(remark)) {
                order.setRemark(remark);
            }
            order.setPriceAgo(g.getPriceAgo());
            double discount = vipDiscount == null ? 1.0 : vipDiscount;
            order.setVipDiscount(discount);
            order.setPriceCount(g.getPrice() * c.getNum() * discount);
            g.setSales((g.getSales() == null ? 0 : g.getSales()) + c.getNum());
            g.setInventory(g.getInventory() - c.getNum());
            goodsService.updateById(g);
            orderService.save(order);
            created.add(order);
        }
        cartService.lambdaUpdate().eq(Cart::getUserId, userId).remove();
        return created;
    }
}
