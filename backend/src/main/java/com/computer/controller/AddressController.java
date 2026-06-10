package com.computer.controller;

import com.computer.common.AuthSupport;
import com.computer.common.Result;
import com.computer.entity.Address;
import com.computer.service.AddressService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Resource
    private AddressService addressService;

    @GetMapping("/list")
    public Result list(HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        List<Address> list = addressService.lambdaQuery().eq(Address::getUserId, userId).list();
        return Result.success(list);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Address address, HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        address.setUserId(userId);
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            addressService.lambdaUpdate().eq(Address::getUserId, userId).set(Address::getIsDefault, false).update();
        }
        addressService.save(address);
        return Result.success("添加成功", null);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Address address, HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        Address db = addressService.getById(address.getAddressId());
        if (db == null || !userId.equals(db.getUserId())) {
            return Result.error(403, "无权操作该地址");
        }
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            addressService.lambdaUpdate().eq(Address::getUserId, userId).set(Address::getIsDefault, false).update();
        }
        address.setUserId(userId);
        addressService.updateById(address);
        return Result.success("修改成功", null);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        Address db = addressService.getById(id);
        if (db == null || !userId.equals(db.getUserId())) {
            return Result.error(403, "无权操作该地址");
        }
        addressService.removeById(id);
        return Result.success("删除成功", null);
    }
}
