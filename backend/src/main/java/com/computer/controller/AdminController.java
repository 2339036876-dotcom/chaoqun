package com.computer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.common.AuthSupport;
import com.computer.common.Result;
import com.computer.entity.User;
import com.computer.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // 获取管理员列表
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page, 
                      @RequestParam(defaultValue = "10") Integer size, 
                      HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        Page<User> adminPage = adminService.listAdmins(page, size);
        return Result.success(adminPage);
    }

    // 添加管理员
    @PostMapping("/add")
    public Result add(@RequestBody User admin, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        boolean success = adminService.addAdmin(admin);
        return success ? Result.success() : Result.error("添加管理员失败");
    }

    // 编辑管理员
    @PutMapping("/edit")
    public Result edit(@RequestBody User admin, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        boolean success = adminService.updateAdmin(admin);
        return success ? Result.success() : Result.error("编辑管理员失败");
    }

    // 删除管理员
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        boolean success = adminService.deleteAdmin(id);
        return success ? Result.success() : Result.error("删除管理员失败");
    }

    // 获取管理员详情
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Integer id, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        User admin = adminService.getAdminById(id);
        return admin != null ? Result.success(admin) : Result.error("管理员不存在");
    }

    // 重置管理员密码
    @PutMapping("/resetPassword/{id}")
    public Result resetPassword(@PathVariable Integer id, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        boolean success = adminService.resetPassword(id);
        return success ? Result.success() : Result.error("重置密码失败");
    }
}
