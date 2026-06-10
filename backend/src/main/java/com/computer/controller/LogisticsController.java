package com.computer.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.common.AuthSupport;
import com.computer.common.Result;
import com.computer.entity.LogisticsDelivery;
import com.computer.service.LogisticsDeliveryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/logistics")
public class LogisticsController {

    @Resource
    private LogisticsDeliveryService logisticsDeliveryService;

    @PostMapping("/add")
    public Result add(@RequestBody LogisticsDelivery delivery, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        if (delivery.getDeliveryStatus() == null) delivery.setDeliveryStatus("未配送");
        if (delivery.getSigningStatus() == null) delivery.setSigningStatus("未签收");
        logisticsDeliveryService.save(delivery);
        return Result.success("添加成功", null);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) String orderNumber,
                       HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        Page<LogisticsDelivery> p = new Page<>(page, size);
        QueryWrapper<LogisticsDelivery> qw = new QueryWrapper<>();
        if (StrUtil.isNotBlank(orderNumber)) qw.eq("order_number", orderNumber);
        qw.orderByDesc("create_time");
        logisticsDeliveryService.page(p, qw);
        return Result.page(p.getRecords(), p.getTotal());
    }

    @PutMapping("/update")
    public Result update(@RequestBody LogisticsDelivery delivery, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        logisticsDeliveryService.updateById(delivery);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        logisticsDeliveryService.removeById(id);
        return Result.success("删除成功", null);
    }
}
