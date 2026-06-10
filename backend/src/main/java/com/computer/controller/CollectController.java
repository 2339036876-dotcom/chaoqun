package com.computer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.common.AuthSupport;
import com.computer.common.Result;
import com.computer.entity.Collect;
import com.computer.service.CollectService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/collect")
public class CollectController {

    @Resource
    private CollectService collectService;

    @PostMapping("/add")
    public Result add(@RequestBody Collect collect, HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        collect.setUserId(userId);
        long count = collectService.lambdaQuery()
                .eq(Collect::getUserId, userId)
                .eq(Collect::getSourceTable, collect.getSourceTable())
                .eq(Collect::getSourceId, collect.getSourceId())
                .count();
        if (count > 0) {
            return Result.error("已收藏");
        }
        collectService.save(collect);
        return Result.success("收藏成功", null);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        Page<Collect> p = new Page<>(page, size);
        QueryWrapper<Collect> qw = new QueryWrapper<>();
        qw.eq("user_id", userId).orderByDesc("create_time");
        collectService.page(p, qw);
        return Result.page(p.getRecords(), p.getTotal());
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        Collect db = collectService.getById(id);
        if (db == null || !userId.equals(db.getUserId())) {
            return Result.error(403, "无权操作收藏记录");
        }
        collectService.removeById(id);
        return Result.success("取消收藏成功", null);
    }

    @GetMapping("/check")
    public Result check(@RequestParam String sourceTable, @RequestParam Integer sourceId, HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        if (userId == null) {
            return Result.success(false);
        }
        long count = collectService.lambdaQuery()
                .eq(Collect::getUserId, userId)
                .eq(Collect::getSourceTable, sourceTable)
                .eq(Collect::getSourceId, sourceId)
                .count();
        return Result.success(count > 0);
    }
}
