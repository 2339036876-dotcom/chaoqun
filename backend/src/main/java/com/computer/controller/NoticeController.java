package com.computer.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.common.AuthSupport;
import com.computer.common.Result;
import com.computer.entity.NoticeAnnouncement;
import com.computer.service.NoticeAnnouncementService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    @Resource
    private NoticeAnnouncementService noticeAnnouncementService;

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) String title) {
        Page<NoticeAnnouncement> p = new Page<>(page, size);
        QueryWrapper<NoticeAnnouncement> qw = new QueryWrapper<>();
        if (StrUtil.isNotBlank(title)) qw.like("notification_title", title);
        qw.orderByDesc("create_time");
        noticeAnnouncementService.page(p, qw);
        return Result.page(p.getRecords(), p.getTotal());
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Integer id) {
        NoticeAnnouncement notice = noticeAnnouncementService.getById(id);
        if (notice != null) {
            notice.setHits(notice.getHits() == null ? 1 : notice.getHits() + 1);
            noticeAnnouncementService.updateById(notice);
        }
        return Result.success(notice);
    }

    @PostMapping("/add")
    public Result add(@RequestBody NoticeAnnouncement notice, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        if (notice.getHits() == null) notice.setHits(0);
        noticeAnnouncementService.save(notice);
        return Result.success("添加成功", null);
    }

    @PutMapping("/update")
    public Result update(@RequestBody NoticeAnnouncement notice, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        noticeAnnouncementService.updateById(notice);
        return Result.success("修改成功", null);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        noticeAnnouncementService.removeById(id);
        return Result.success("删除成功", null);
    }
}
