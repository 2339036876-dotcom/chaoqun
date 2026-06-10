package com.computer.controller;

import com.computer.common.AuthSupport;
import com.computer.common.Result;
import com.computer.entity.Goods;
import com.computer.entity.GoodsType;
import com.computer.service.GoodsService;
import com.computer.service.GoodsTypeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/goods/type")
public class GoodsTypeController {

    @Resource
    private GoodsTypeService goodsTypeService;
    @Resource
    private GoodsService goodsService;

    @GetMapping("/list")
    public Result list() {
        List<GoodsType> list = goodsTypeService.list();
        return Result.success(list);
    }

    /**
     * 各分类下商品数量（用于商家端统计图，含商品数为 0 的分类）
     */
    @GetMapping("/stats/count-by-type")
    public Result countByType() {
        List<GoodsType> types = goodsTypeService.list();
        List<Map<String, Object>> rows = new ArrayList<>();
        for (GoodsType t : types) {
            long cnt = goodsService.lambdaQuery().eq(Goods::getType, t.getTypeId()).count();
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("typeId", t.getTypeId());
            row.put("name", t.getName());
            row.put("count", cnt);
            rows.add(row);
        }
        return Result.success(rows);
    }

    @PostMapping("/add")
    public Result add(@RequestBody GoodsType goodsType, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        
        if (goodsType.getName() == null || goodsType.getName().trim().isEmpty()) {
            return Result.error("请填写分类名称");
        }
        
        long count = goodsTypeService.lambdaQuery()
                .eq(GoodsType::getName, goodsType.getName().trim())
                .count();
        if (count > 0) {
            return Result.error("已存在分类名称，添加失败");
        }
        
        goodsType.setTypeId(null);
        goodsTypeService.save(goodsType);
        return Result.success("分类添加成功，显示在列表", null);
    }

    @PutMapping("/update")
    public Result update(@RequestBody GoodsType goodsType, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        
        if (goodsType.getTypeId() == null) {
            return Result.error("分类ID不能为空");
        }
        
        GoodsType existingType = goodsTypeService.getById(goodsType.getTypeId());
        if (existingType == null) {
            return Result.error("分类不存在");
        }
        
        if (goodsType.getName() != null && !goodsType.getName().trim().isEmpty()) {
            long count = goodsTypeService.lambdaQuery()
                    .eq(GoodsType::getName, goodsType.getName().trim())
                    .ne(GoodsType::getTypeId, goodsType.getTypeId())
                    .count();
            if (count > 0) {
                return Result.error("分类名称已存在");
            }
        }
        
        goodsTypeService.updateById(goodsType);
        return Result.success("修改成功", null);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        
        GoodsType goodsType = goodsTypeService.getById(id);
        if (goodsType == null) {
            return Result.error("分类不存在");
        }
        
        long goodsCount = goodsService.lambdaQuery().eq(Goods::getType, id).count();
        if (goodsCount > 0) {
            return Result.error("该分类下有商品，无法删除");
        }
        
        goodsTypeService.removeById(id);
        return Result.success("删除成功", null);
    }
}
