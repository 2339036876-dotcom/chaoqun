package com.computer.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.common.AuthSupport;
import com.computer.common.Result;
import com.computer.entity.Goods;
import com.computer.entity.LogisticsDelivery;
import com.computer.entity.Order;
import com.computer.service.GoodsService;
import com.computer.service.LogisticsDeliveryService;
import com.computer.service.OrderCancelService;
import com.computer.service.OrderCheckoutService;
import com.computer.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Resource
    private OrderService orderService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private LogisticsDeliveryService logisticsDeliveryService;
    @Resource
    private OrderCheckoutService orderCheckoutService;
    @Resource
    private OrderCancelService orderCancelService;

    @PutMapping("/cancel")
    public Result cancel(@RequestBody Order order, HttpServletRequest request) {
        if (order.getOrderId() == null) {
            return Result.error("订单ID不能为空");
        }
        Integer userId = AuthSupport.getUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        try {
            orderCancelService.userCancel(userId, order.getOrderId());
            return Result.success("订单已取消", null);
        } catch (IllegalStateException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/checkout-cart")
    public Result checkoutCart(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        String contactName = body.get("contactName") == null ? null : String.valueOf(body.get("contactName"));
        String contactPhone = body.get("contactPhone") == null ? null : String.valueOf(body.get("contactPhone"));
        String contactAddress = body.get("contactAddress") == null ? null : String.valueOf(body.get("contactAddress"));
        String remark = body.get("remark") == null ? null : String.valueOf(body.get("remark"));
        Double vipDiscount = null;
        if (body.get("vipDiscount") instanceof Number) {
            vipDiscount = ((Number) body.get("vipDiscount")).doubleValue();
        }
        if (StrUtil.isBlank(contactName) || StrUtil.isBlank(contactPhone) || StrUtil.isBlank(contactAddress)) {
            return Result.error("请填写完整收货信息");
        }
        try {
            List<Order> orders = orderCheckoutService.checkoutCart(userId, contactName, contactPhone, contactAddress, remark, vipDiscount);
            return Result.success("下单成功", orders);
        } catch (IllegalStateException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/add")
    public Result add(@RequestBody Order order, HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        order.setUserId(userId);
        order.setOrderNumber(IdUtil.getSnowflakeNextIdStr());
        if (order.getNum() == null || order.getNum() <= 0) {
            order.setNum(1);
        }
        if (StrUtil.isBlank(order.getState())) {
            order.setState("待付款");
        }
        if (StrUtil.isBlank(order.getDeliveryState())) {
            order.setDeliveryState("未配送");
        }
        Goods goods = goodsService.getById(order.getGoodsId());
        if (goods == null) {
            return Result.error("商品不存在");
        }
        if (goods.getInventory() == null || goods.getInventory() < order.getNum()) {
            return Result.error("库存不足");
        }
        order.setPriceAgo(goods.getPriceAgo());
        double discount = order.getVipDiscount() == null ? 1.0 : order.getVipDiscount();
        order.setPriceCount(goods.getPrice() * order.getNum() * discount);
        goods.setSales((goods.getSales() == null ? 0 : goods.getSales()) + order.getNum());
        goods.setInventory(goods.getInventory() - order.getNum());
        goodsService.updateById(goods);
        orderService.save(order);
        return Result.success("下单成功", order);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) String state,
                       @RequestParam(required = false) Integer userId,
                       HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        Page<Order> p = new Page<>(page, size);
        QueryWrapper<Order> qw = new QueryWrapper<>();
        if (userId != null) qw.eq("user_id", userId);
        if (StrUtil.isNotBlank(state)) qw.eq("state", state);
        qw.orderByDesc("create_time");
        orderService.page(p, qw);
        return Result.page(p.getRecords(), p.getTotal());
    }

    @GetMapping("/my")
    public Result myOrders(@RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "10") Integer size,
                           @RequestParam(required = false) String state,
                           HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        Page<Order> p = new Page<>(page, size);
        QueryWrapper<Order> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        if (StrUtil.isNotBlank(state)) qw.eq("state", state);
        qw.orderByDesc("create_time");
        orderService.page(p, qw);
        return Result.page(p.getRecords(), p.getTotal());
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Integer id, HttpServletRequest request) {
        Order order = orderService.getById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        Integer userId = AuthSupport.getUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        if (!AuthSupport.isAdmin(request) && !userId.equals(order.getUserId())) {
            return Result.error(403, "无权查看该订单");
        }
        return Result.success(order);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Order order, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        orderService.updateById(order);
        return Result.success("更新成功", null);
    }

    @PutMapping("/state")
    public Result updateState(@RequestBody Order order, HttpServletRequest request) {
        Order db = orderService.getById(order.getOrderId());
        if (db == null) {
            return Result.error("订单不存在");
        }
        Integer userId = AuthSupport.getUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        if (!AuthSupport.isAdmin(request) && !userId.equals(db.getUserId())) {
            return Result.error(403, "无权操作该订单");
        }
        Order update = new Order();
        update.setOrderId(order.getOrderId());
        // 当用户确认收货时，将订单状态改为"已签收"
        if ("已完成".equals(order.getState())) {
            update.setState("已签收");
        } else {
            update.setState(order.getState());
        }
        orderService.updateById(update);

        if ("已完成".equals(order.getState())) {
            LogisticsDelivery logistics = logisticsDeliveryService.lambdaQuery()
                    .eq(LogisticsDelivery::getOrderNumber, db.getOrderNumber())
                    .one();
            if (logistics != null) {
                logistics.setSigningStatus("已签收");
                logistics.setDeliveryStatus("已配送");
                logisticsDeliveryService.updateById(logistics);
            }
        }
        return Result.success("状态更新成功", null);
    }

    @PutMapping("/delivery")
    public Result delivery(@RequestBody Order order, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        Order db = orderService.getById(order.getOrderId());
        if (db == null) {
            return Result.error("订单不存在");
        }
        Order update = new Order();
        update.setOrderId(order.getOrderId());
        update.setDeliveryState(order.getDeliveryState());
        if ("已配送".equals(order.getDeliveryState())) {
            update.setState("待签收");
        }
        orderService.updateById(update);

        if ("已配送".equals(order.getDeliveryState())) {
            Goods goods = goodsService.getById(db.getGoodsId());
            LogisticsDelivery logistics = logisticsDeliveryService.lambdaQuery()
                    .eq(LogisticsDelivery::getOrderNumber, db.getOrderNumber())
                    .one();
            if (logistics == null) {
                logistics = new LogisticsDelivery();
                logistics.setOrderNumber(db.getOrderNumber());
                logistics.setProductName(goods != null ? goods.getTitle() : "商品");
                logistics.setPurchaseQuantity(String.valueOf(db.getNum()));
                logistics.setTotalTransactionAmount(db.getPriceCount());
                logistics.setDeliveryStatus("已配送");
                logistics.setSigningStatus("未签收");
                logistics.setContactName(db.getContactName());
                logisticsDeliveryService.save(logistics);
            } else {
                logistics.setDeliveryStatus("已配送");
                logistics.setSigningStatus("未签收");
                logisticsDeliveryService.updateById(logistics);
            }
        }
        return Result.success("发货成功", null);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        orderService.removeById(id);
        return Result.success("删除成功", null);
    }
}
