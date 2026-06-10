package com.computer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.common.AuthSupport;
import com.computer.common.JwtUtil;
import com.computer.common.Result;
import com.computer.entity.Comment;
import com.computer.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @GetMapping("/list/{sourceTable}/{sourceId}")
    public Result list(@PathVariable String sourceTable,
                       @PathVariable Integer sourceId,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size) {
        Page<Comment> p = new Page<>(page, size);
        QueryWrapper<Comment> qw = new QueryWrapper<>();
        qw.eq("source_table", sourceTable);
        if (sourceId != null && sourceId > 0) {
            qw.eq("source_id", sourceId);
        }
        qw.orderByDesc("create_time");
        commentService.page(p, qw);
        return Result.page(p.getRecords(), p.getTotal());
    }

    @PostMapping("/add")
    public Result add(@RequestBody Comment comment, HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        comment.setUserId(userId);
        commentService.save(comment);
        return Result.success("评论成功", null);
    }

    @PutMapping("/reply")
    public Result reply(@RequestBody Comment comment, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        Comment update = new Comment();
        update.setCommentId(comment.getCommentId());
        update.setReply(comment.getReply());
        commentService.updateById(update);
        return Result.success("回复成功", null);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        commentService.removeById(id);
        return Result.success("删除成功", null);
    }
}
