package com.computer.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.common.AuthSupport;
import com.computer.common.Result;
import com.computer.entity.Article;
import com.computer.entity.ArticleType;
import com.computer.service.ArticleService;
import com.computer.service.ArticleTypeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;
    @Resource
    private ArticleTypeService articleTypeService;

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) String title,
                       @RequestParam(required = false) Integer type) {
        Page<Article> p = new Page<>(page, size);
        QueryWrapper<Article> qw = new QueryWrapper<>();
        if (StrUtil.isNotBlank(title)) qw.like("title", title);
        if (type != null) qw.eq("type", type);
        qw.orderByDesc("create_time");
        articleService.page(p, qw);
        return Result.page(p.getRecords(), p.getTotal());
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Integer id) {
        Article article = articleService.getById(id);
        if (article != null) {
            article.setHits(article.getHits() == null ? 1 : article.getHits() + 1);
            articleService.updateById(article);
        }
        return Result.success(article);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Article article, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        if (article.getHits() == null) article.setHits(0);
        if (article.getPraiseLen() == null) article.setPraiseLen(0);
        articleService.save(article);
        return Result.success("添加成功", null);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Article article, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        articleService.updateById(article);
        return Result.success("修改成功", null);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        articleService.removeById(id);
        return Result.success("删除成功", null);
    }

    @GetMapping("/type/list")
    public Result typeList() {
        List<ArticleType> list = articleTypeService.list();
        return Result.success(list);
    }

    @PostMapping("/type/add")
    public Result addType(@RequestBody ArticleType type, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        articleTypeService.save(type);
        return Result.success("添加成功", null);
    }

    @PutMapping("/type/update")
    public Result updateType(@RequestBody ArticleType type, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        articleTypeService.updateById(type);
        return Result.success("修改成功", null);
    }

    @DeleteMapping("/type/delete/{id}")
    public Result deleteType(@PathVariable Integer id, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        articleTypeService.removeById(id);
        return Result.success("删除成功", null);
    }
}
