package com.computer.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.common.AuthSupport;
import com.computer.common.JwtUtil;
import com.computer.common.Result;
import com.computer.entity.User;
import com.computer.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody User loginUser) {
        if (StrUtil.isBlank(loginUser.getUsername()) || StrUtil.isBlank(loginUser.getPassword())) {
            return Result.error("请输入用户名/密码");
        }
        User user = userService.lambdaQuery()
                .eq(User::getUsername, loginUser.getUsername())
                .one();
        if (user == null) {
            return Result.error("用户名不存在");
        }
        if (!loginUser.getPassword().equals(user.getPassword())) {
            return Result.error("密码错误");
        }
        if (user.getState() != null && user.getState() != 1) {
            return Result.error("账号状态异常");
        }
        user.setLoginTime(new Timestamp(System.currentTimeMillis()));
        userService.updateById(user);
        String token = JwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getUserGroup());
        user.setPassword(null);
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("token", token);
        return Result.success("登录成功，跳转到首页", data);
    }

    @PostMapping("/admin/login")
    public Result adminLogin(@RequestBody User loginUser) {
        if (StrUtil.isBlank(loginUser.getUsername()) || StrUtil.isBlank(loginUser.getPassword())) {
            return Result.error("用户名和密码不能为空");
        }
        User user = userService.lambdaQuery()
                .eq(User::getUsername, loginUser.getUsername())
                .eq(User::getPassword, loginUser.getPassword())
                .one();
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        if (user.getState() != null && user.getState() != 1) {
            return Result.error("账号状态异常");
        }
        // 后端只能管理员登录
        if (user.getUserGroup() != null && user.getUserGroup() != 1) {
            return Result.error("用户账号禁止登录后端");
        }
        user.setLoginTime(new Timestamp(System.currentTimeMillis()));
        userService.updateById(user);
        String token = JwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getUserGroup());
        user.setPassword(null);
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("token", token);
        return Result.success("登录成功", data);
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("请填写完整注册信息");
        }
        
        long count = userService.lambdaQuery().eq(User::getUsername, user.getUsername()).count();
        if (count > 0) {
            return Result.error("用户名已存在");
        }
        
        if (user.getPassword().length() < 6) {
            return Result.error("密码长度不足六位");
        }
        
        if (!user.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            return Result.error("邮箱格式不正确");
        }
        
        if (StrUtil.isNotBlank(user.getPhone()) && !user.getPhone().matches("^1[3-9]\\d{9}$")) {
            return Result.error("手机格式不正确");
        }
        
        user.setState(1);
        user.setUserGroup(AuthSupport.USER_GROUP);
        if (StrUtil.isBlank(user.getNickname())) {
            user.setNickname(user.getUsername());
        }
        userService.save(user);
        return Result.success("注册成功，跳转到登录页面", null);
    }

    @GetMapping("/info")
    public Result info(HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        User user = userService.getById(userId);
        if (user != null) {
            user.setPassword(null);
        }
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody User user, HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        user.setUserId(userId);
        user.setPassword(null);
        user.setUserGroup(null);
        user.setState(null);
        userService.updateById(user);
        return Result.success("修改成功", null);
    }

    @PutMapping("/password")
    public Result updatePassword(@RequestBody Map<String, String> params, HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        if (StrUtil.isBlank(oldPassword) || StrUtil.isBlank(newPassword)) {
            return Result.error("请输入完整密码");
        }
        User user = userService.getById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        if (!oldPassword.equals(user.getPassword())) {
            return Result.error("原密码错误");
        }
        user.setPassword(newPassword);
        userService.updateById(user);
        return Result.success("密码修改成功", null);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) String username,
                       HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        Page<User> p = new Page<>(page, size);
        QueryWrapper<User> qw = new QueryWrapper<>();
        // 只查询普通用户（user_group=2），不包括管理员
        qw.eq("user_group", 2);
        if (StrUtil.isNotBlank(username)) {
            qw.like("username", username);
        }
        qw.orderByDesc("create_time");
        userService.page(p, qw);
        p.getRecords().forEach(item -> item.setPassword(null));
        return Result.page(p.getRecords(), p.getTotal());
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        userService.removeById(id);
        return Result.success("删除成功", null);
    }

    @PutMapping("/state")
    public Result updateState(@RequestBody User user, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        User update = new User();
        update.setUserId(user.getUserId());
        update.setState(user.getState());
        userService.updateById(update);
        return Result.success("状态更新成功", null);
    }
}
