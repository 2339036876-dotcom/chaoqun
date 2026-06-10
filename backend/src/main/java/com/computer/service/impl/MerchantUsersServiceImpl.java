package com.computer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.entity.MerchantUsers;
import com.computer.mapper.MerchantUsersMapper;
import com.computer.service.MerchantUsersService;
import org.springframework.stereotype.Service;

@Service
public class MerchantUsersServiceImpl extends ServiceImpl<MerchantUsersMapper, MerchantUsers> implements MerchantUsersService {
}
