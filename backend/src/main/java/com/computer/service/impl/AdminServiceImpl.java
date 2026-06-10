package com.computer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.entity.User;
import com.computer.mapper.UserMapper;
import com.computer.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Page<User> listAdmins(Integer page, Integer size) {
        Page<User> adminPage = new Page<>(page, size);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_group", 1); // 1表示管理员组
        return userMapper.selectPage(adminPage, wrapper);
    }

    @Override
    public boolean addAdmin(User admin) {
        admin.setUserGroup(1); // 设置为管理员组
        admin.setState(admin.getState() != null ? admin.getState() : 1); // 设置状态
        // 确保密码不为空
        if (admin.getPassword() == null || admin.getPassword().isEmpty()) {
            admin.setPassword("123456"); // 默认密码
        }
        // 确保昵称为空时使用用户名作为昵称
        if (admin.getNickname() == null || admin.getNickname().isEmpty()) {
            admin.setNickname(admin.getUsername());
        }
        return userMapper.insert(admin) > 0;
    }

    @Override
    public boolean updateAdmin(User admin) {
        return userMapper.updateById(admin) > 0;
    }

    @Override
    public boolean deleteAdmin(Integer id) {
        // 不能删除超级管理员
        User admin = userMapper.selectById(id);
        if (admin != null && "admin".equals(admin.getUsername())) {
            return false;
        }
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public User getAdminById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public boolean resetPassword(Integer id) {
        User admin = new User();
        admin.setUserId(id);
        admin.setPassword("123456"); // 重置为默认密码
        return userMapper.updateById(admin) > 0;
    }
}
