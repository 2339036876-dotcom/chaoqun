package com.computer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.common.JwtUtil;
import com.computer.common.Result;
import com.computer.entity.Schedule;
import com.computer.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Resource
    private ScheduleService scheduleService;

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       HttpServletRequest request) {
        Integer userId = JwtUtil.getUserId(request.getHeader("token"));
        Page<Schedule> p = new Page<>(page, size);
        QueryWrapper<Schedule> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        qw.orderByDesc("create_time");
        scheduleService.page(p, qw);
        return Result.page(p.getRecords(), p.getTotal());
    }

    @PostMapping("/add")
    public Result add(@RequestBody Schedule schedule, HttpServletRequest request) {
        Integer userId = JwtUtil.getUserId(request.getHeader("token"));
        schedule.setUserId(userId);
        scheduleService.save(schedule);
        return Result.success("添加成功", null);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Schedule schedule, HttpServletRequest request) {
        Integer userId = JwtUtil.getUserId(request.getHeader("token"));
        Schedule old = scheduleService.getById(schedule.getScheduleId());
        if (old == null || !old.getUserId().equals(userId)) {
            return Result.error(403, "无权限操作");
        }
        scheduleService.updateById(schedule);
        return Result.success("修改成功", null);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        Integer userId = JwtUtil.getUserId(request.getHeader("token"));
        Schedule old = scheduleService.getById(id);
        if (old == null || !old.getUserId().equals(userId)) {
            return Result.error(403, "无权限操作");
        }
        scheduleService.removeById(id);
        return Result.success("删除成功", null);
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Integer id, HttpServletRequest request) {
        Integer userId = JwtUtil.getUserId(request.getHeader("token"));
        Schedule schedule = scheduleService.getById(id);
        if (schedule == null || !schedule.getUserId().equals(userId)) {
            return Result.error(403, "无权限查看");
        }
        return Result.success(schedule);
    }
}
