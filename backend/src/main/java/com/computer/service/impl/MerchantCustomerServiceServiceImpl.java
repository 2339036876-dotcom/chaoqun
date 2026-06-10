package com.computer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.entity.MerchantCustomerService;
import com.computer.mapper.MerchantCustomerServiceMapper;
import com.computer.service.MerchantCustomerServiceService;
import org.springframework.stereotype.Service;

@Service
public class MerchantCustomerServiceServiceImpl extends ServiceImpl<MerchantCustomerServiceMapper, MerchantCustomerService> implements MerchantCustomerServiceService {
}
