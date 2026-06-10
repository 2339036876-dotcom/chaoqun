package com.computer.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.common.AuthSupport;
import com.computer.common.JwtUtil;
import com.computer.common.Result;
import com.computer.entity.Forum;
import com.computer.entity.ForumType;
import com.computer.entity.User;
import com.computer.service.ForumService;
import com.computer.service.ForumTypeService;
import com.computer.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/forum")
public class ForumController {

    @Resource
    private ForumService forumService;
    @Resource
    private ForumTypeService forumTypeService;
    @Resource
    private UserService userService;

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) String title,
                       @RequestParam(required = false) String type) {
        Page<Forum> p = new Page<>(page, size);
        QueryWrapper<Forum> qw = new QueryWrapper<>();
        if (StrUtil.isNotBlank(title)) qw.like("title", title);
        if (StrUtil.isNotBlank(type)) qw.eq("type", type);
        qw.orderByDesc("istop").orderByDesc("create_time");
        forumService.page(p, qw);
        return Result.page(p.getRecords(), p.getTotal());
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Integer id) {
        Forum forum = forumService.getById(id);
        if (forum != null) {
            forum.setHits(forum.getHits() == null ? 1 : forum.getHits() + 1);
            forumService.updateById(forum);
        }
        return Result.success(forum);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Forum forum, HttpServletRequest request) {
        Integer userId = JwtUtil.getUserId(request.getHeader("token"));
        forum.setUserId(userId);
        User user = userService.getById(userId);
        if (user != null) {
            forum.setNickname(user.getNickname());
            forum.setAvatar(user.getAvatar());
        }
        if (forum.getHits() == null) forum.setHits(0);
        if (forum.getPraiseLen() == null) forum.setPraiseLen(0);
        if (forum.getIstop() == null) forum.setIstop(0);
        if (forum.getDisplay() == null) forum.setDisplay(0);
        forumService.save(forum);
        return Result.success("发布成功", null);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Forum forum, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        forumService.updateById(forum);
        return Result.success("修改成功", null);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        forumService.removeById(id);
        return Result.success("删除成功", null);
    }

    @GetMapping("/type/list")
    public Result typeList() {
        List<ForumType> list = forumTypeService.list();
        return Result.success(list);
    }

    @PostMapping("/type/add")
    public Result addType(@RequestBody ForumType type, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        forumTypeService.save(type);
        return Result.success("添加成功", null);
    }

    @PutMapping("/type/update")
    public Result updateType(@RequestBody ForumType type, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        forumTypeService.updateById(type);
        return Result.success("修改成功", null);
    }

    @DeleteMapping("/type/delete/{id}")
    public Result deleteType(@PathVariable Integer id, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        forumTypeService.removeById(id);
        return Result.success("删除成功", null);
    }
}
