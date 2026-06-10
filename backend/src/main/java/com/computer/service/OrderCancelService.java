package com.computer.service;

import com.computer.entity.Goods;
import com.computer.entity.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class OrderCancelService {

    @Resource
    private OrderService orderService;
    @Resource
    private GoodsService goodsService;

    @Transactional(rollbackFor = Exception.class)
    public void userCancel(Integer userId, Integer orderId) {
        Order db = orderService.getById(orderId);
        if (db == null) {
            throw new IllegalStateException("订单不存在");
        }
        if (!userId.equals(db.getUserId())) {
            throw new IllegalStateException("无权操作该订单");
        }
        if (!"待付款".equals(db.getState()) && !"待发货".equals(db.getState())) {
            throw new IllegalStateException("当前状态不可取消");
        }
        Goods g = goodsService.getById(db.getGoodsId());
        if (g != null) {
            int n = db.getNum() == null ? 0 : db.getNum();
            g.setInventory((g.getInventory() == null ? 0 : g.getInventory()) + n);
            int sales = (g.getSales() == null ? 0 : g.getSales()) - n;
            g.setSales(Math.max(0, sales));
            goodsService.updateById(g);
        }
        Order update = new Order();
        update.setOrderId(orderId);
        update.setState("已取消");
        orderService.updateById(update);
    }
}
