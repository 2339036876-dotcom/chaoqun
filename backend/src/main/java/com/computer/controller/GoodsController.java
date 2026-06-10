package com.computer.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.common.AuthSupport;
import com.computer.common.Result;
import com.computer.entity.Goods;
import com.computer.entity.GoodsType;
import com.computer.entity.Order;
import com.computer.service.GoodsService;
import com.computer.service.GoodsTypeService;
import com.computer.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {

    @Resource
    private GoodsService goodsService;
    
    @Resource
    private OrderService orderService;
    
    @Resource
    private GoodsTypeService goodsTypeService;

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) String title,
                       @RequestParam(required = false) Integer type,
                       @RequestParam(required = false) String sort) {
        Page<Goods> p = new Page<>(page, size);
        QueryWrapper<Goods> qw = new QueryWrapper<>();
        
        if (StrUtil.isNotBlank(title)) {
            if (title.length() > 100) {
                return Result.error("关键词过长，请输入不超过50个字符");
            }
            
            if (title.matches("^[^a-zA-Z0-9\\u4e00-\\u9fa5]+$")) {
                return Result.success("未找到相关商品");
            }
            
            qw.like("title", title);
        }
        
        if (type != null) {
            long typeCount = goodsTypeService.lambdaQuery().eq(GoodsType::getTypeId, type).count();
            if (typeCount == 0) {
                return Result.success("暂无商品");
            }
            qw.eq("type", type);
        }
        
        if ("sales".equals(sort)) {
            qw.orderByDesc("sales");
        } else if ("price_asc".equals(sort)) {
            qw.orderByAsc("price");
        } else if ("price_desc".equals(sort)) {
            qw.orderByDesc("price");
        } else if ("hits".equals(sort)) {
            qw.orderByDesc("hits");
        } else {
            qw.orderByDesc("create_time");
        }
        goodsService.page(p, qw);
        return Result.page(p.getRecords(), p.getTotal());
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Integer id) {
        Goods goods = goodsService.getById(id);
        if (goods == null) {
            return Result.error("提示\"商品不存在\"");
        }
        goods.setHits(goods.getHits() == null ? 1 : goods.getHits() + 1);
        goodsService.updateById(goods);
        return Result.success(goods);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Goods goods, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        
        if (StrUtil.isBlank(goods.getTitle())) {
            return Result.error("请填写完整商品信息，添加失败");
        }
        
        if (goods.getPrice() != null && goods.getPrice() < 0) {
            return Result.error("价格不能为负数，添加失败");
        }
        
        if (goods.getInventory() != null && goods.getInventory() < 0) {
            return Result.error("库存不能为负数，添加失败");
        }
        
        goods.setUserId(AuthSupport.getUserId(request));
        if (goods.getSales() == null) goods.setSales(0);
        if (goods.getHits() == null) goods.setHits(0);
        goodsService.save(goods);
        return Result.success("商品添加成功，显示在列表", null);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Goods goods, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        
        if (goods.getGoodsId() == null) {
            return Result.error("商品ID不能为空");
        }
        
        Goods existingGoods = goodsService.getById(goods.getGoodsId());
        if (existingGoods == null) {
            return Result.error("商品不存在，修改失败");
        }
        
        Integer currentUserId = AuthSupport.getUserId(request);
        if (!currentUserId.equals(existingGoods.getUserId())) {
            return Result.error("无权修改该商品，修改失败");
        }
        
        if (goods.getPrice() != null && goods.getPrice() < 0) {
            return Result.error("价格不能为负数");
        }
        
        if (goods.getInventory() != null && goods.getInventory() < 0) {
            return Result.error("库存不能为负数");
        }
        
        goodsService.updateById(goods);
        return Result.success("商品信息更新成功", null);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        
        Goods goods = goodsService.getById(id);
        if (goods == null) {
            return Result.error("商品不存在");
        }
        
        Integer currentUserId = AuthSupport.getUserId(request);
        if (!currentUserId.equals(goods.getUserId())) {
            return Result.error("无权删除该商品");
        }
        
        long orderCount = orderService.lambdaQuery().eq(Order::getGoodsId, id).count();
        System.out.println("商品ID: " + id + ", 关联订单数: " + orderCount);
        if (orderCount > 0) {
            return Result.error("该商品已有订单关联，无法删除");
        }
        
        goodsService.removeById(id);
        return Result.success("商品从列表移除", null);
    }
}
