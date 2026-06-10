package com.computer.controller;

import com.computer.common.AuthSupport;
import com.computer.common.Result;
import com.computer.entity.UserGroup;
import com.computer.service.UserGroupService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/userGroup")
public class UserGroupController {

    @Resource
    private UserGroupService userGroupService;

    @GetMapping("/list")
    public Result list(HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        List<UserGroup> list = userGroupService.list();
        return Result.success(list);
    }

    @PostMapping("/add")
    public Result add(@RequestBody UserGroup userGroup, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        userGroupService.save(userGroup);
        return Result.success("添加成功", null);
    }

    @PutMapping("/update")
    public Result update(@RequestBody UserGroup userGroup, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        userGroupService.updateById(userGroup);
        return Result.success("修改成功", null);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        userGroupService.removeById(id);
        return Result.success("删除成功", null);
    }
}
