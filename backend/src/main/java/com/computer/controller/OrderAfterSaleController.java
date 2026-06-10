package com.computer.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.common.AuthSupport;
import com.computer.common.Result;
import com.computer.entity.Order;
import com.computer.entity.OrderAfterSale;
import com.computer.service.OrderAfterSaleService;
import com.computer.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/order/afterSale")
public class OrderAfterSaleController {

    @Resource
    private OrderAfterSaleService orderAfterSaleService;
    @Resource
    private OrderService orderService;

    @PostMapping("/add")
    public Result add(@RequestBody OrderAfterSale afterSale, HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        Order order = orderService.getById(afterSale.getOrderId());
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (!userId.equals(order.getUserId())) {
            return Result.error(403, "无权申请该订单售后");
        }
        if (StrUtil.isBlank(afterSale.getAfterState())) {
            afterSale.setAfterState("未审核");
        }
        orderAfterSaleService.save(afterSale);
        Order update = new Order();
        update.setOrderId(afterSale.getOrderId());
        update.setState("待退款");
        orderService.updateById(update);
        return Result.success("提交成功", null);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) String afterState,
                       HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        Page<OrderAfterSale> p = new Page<>(page, size);
        QueryWrapper<OrderAfterSale> qw = new QueryWrapper<>();
        if (StrUtil.isNotBlank(afterState)) qw.eq("after_state", afterState);
        qw.orderByDesc("create_time");
        orderAfterSaleService.page(p, qw);
        return Result.page(p.getRecords(), p.getTotal());
    }

    @PutMapping("/review")
    public Result review(@RequestBody OrderAfterSale afterSale, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        orderAfterSaleService.updateById(afterSale);
        Order update = new Order();
        update.setOrderId(afterSale.getOrderId());
        if ("已通过".equals(afterSale.getAfterState())) {
            update.setState("已退款");
        } else if ("未通过".equals(afterSale.getAfterState())) {
            update.setState("已拒绝");
        }
        if (update.getState() != null) {
            orderService.updateById(update);
        }
        return Result.success("审核完成", null);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        orderAfterSaleService.removeById(id);
        return Result.success("删除成功", null);
    }
}
