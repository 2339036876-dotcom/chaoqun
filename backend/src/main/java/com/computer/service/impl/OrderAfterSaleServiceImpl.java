package com.computer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.entity.OrderAfterSale;
import com.computer.mapper.OrderAfterSaleMapper;
import com.computer.service.OrderAfterSaleService;
import org.springframework.stereotype.Service;

@Service
public class OrderAfterSaleServiceImpl extends ServiceImpl<OrderAfterSaleMapper, OrderAfterSale> implements OrderAfterSaleService {
}
