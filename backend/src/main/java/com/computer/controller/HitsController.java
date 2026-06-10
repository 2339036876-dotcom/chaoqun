package com.computer.controller;

import com.computer.common.JwtUtil;
import com.computer.common.Result;
import com.computer.entity.Hits;
import com.computer.service.HitsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/hits")
public class HitsController {

    @Resource
    private HitsService hitsService;

    @PostMapping("/add")
    public Result add(@RequestBody Hits hits, HttpServletRequest request) {
        Integer userId = JwtUtil.getUserId(request.getHeader("token"));
        hits.setUserId(userId);
        long count = hitsService.lambdaQuery()
                .eq(Hits::getUserId, userId)
                .eq(Hits::getSourceTable, hits.getSourceTable())
                .eq(Hits::getSourceId, hits.getSourceId())
                .count();
        if (count == 0) {
            hitsService.save(hits);
        }
        return Result.success();
    }
}
