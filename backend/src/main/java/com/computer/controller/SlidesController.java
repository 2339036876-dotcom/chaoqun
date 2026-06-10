package com.computer.controller;

import com.computer.common.AuthSupport;
import com.computer.common.Result;
import com.computer.entity.Slides;
import com.computer.service.SlidesService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/slides")
public class SlidesController {

    @Resource
    private SlidesService slidesService;

    @GetMapping("/list")
    public Result list() {
        List<Slides> list = slidesService.list();
        return Result.success(list);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Slides slides, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        if (slides.getHits() == null) slides.setHits(0);
        slidesService.save(slides);
        return Result.success("添加成功", null);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Slides slides, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        slidesService.updateById(slides);
        return Result.success("修改成功", null);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        slidesService.removeById(id);
        return Result.success("删除成功", null);
    }
}
