package com.computer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.computer.common.JwtUtil;
import com.computer.common.Result;
import com.computer.entity.Score;
import com.computer.entity.User;
import com.computer.service.ScoreService;
import com.computer.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/score")
public class ScoreController {

    @Resource
    private ScoreService scoreService;
    @Resource
    private UserService userService;

    @PostMapping("/add")
    public Result add(@RequestBody Score score, HttpServletRequest request) {
        Integer userId = JwtUtil.getUserId(request.getHeader("token"));
        score.setUserId(userId);
        User user = userService.getById(userId);
        if (user != null) score.setNickname(user.getNickname());
        long count = scoreService.lambdaQuery()
                .eq(Score::getUserId, userId)
                .eq(Score::getSourceTable, score.getSourceTable())
                .eq(Score::getSourceId, score.getSourceId())
                .count();
        if (count > 0) return Result.error("已评分");
        scoreService.save(score);
        return Result.success("评分成功", null);
    }

    @GetMapping("/list/{sourceTable}/{sourceId}")
    public Result list(@PathVariable String sourceTable, @PathVariable Integer sourceId) {
        List<Score> list = scoreService.lambdaQuery()
                .eq(Score::getSourceTable, sourceTable)
                .eq(Score::getSourceId, sourceId)
                .list();
        double avg = list.stream().mapToDouble(Score::getScoreNum).average().orElse(0);
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("avg", Math.round(avg * 10) / 10.0);
        map.put("count", list.size());
        return Result.success(map);
    }
}
