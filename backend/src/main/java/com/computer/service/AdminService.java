package com.computer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.entity.User;

public interface AdminService {

    // 获取管理员列表
    Page<User> listAdmins(Integer page, Integer size);

    // 添加管理员
    boolean addAdmin(User admin);

    // 更新管理员
    boolean updateAdmin(User admin);

    // 删除管理员
    boolean deleteAdmin(Integer id);

    // 根据ID获取管理员
    User getAdminById(Integer id);

    // 重置管理员密码
    boolean resetPassword(Integer id);
}
