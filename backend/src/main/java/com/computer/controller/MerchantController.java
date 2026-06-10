package com.computer.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.common.AuthSupport;
import com.computer.common.Result;
import com.computer.entity.Goods;
import com.computer.entity.MerchantCustomerService;
import com.computer.entity.MerchantUsers;
import com.computer.service.GoodsService;
import com.computer.service.MerchantCustomerServiceService;
import com.computer.service.MerchantUsersService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/merchant")
public class MerchantController {

    private static final String SENDER_USER = "user";
    private static final String SENDER_MERCHANT = "merchant";
    private static final String READ_UNREAD = "未读";
    private static final String READ_READ = "已读";

    @Resource
    private MerchantUsersService merchantUsersService;
    @Resource
    private MerchantCustomerServiceService merchantCustomerServiceService;
    @Resource
    private GoodsService goodsService;

    @PostMapping("/apply")
    public Result apply(@RequestBody MerchantUsers merchantUsers, HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        long count = merchantUsersService.lambdaQuery().eq(MerchantUsers::getUserId, userId).count();
        if (count > 0) {
            return Result.error("你已经提交过商家申请");
        }
        merchantUsers.setUserId(userId);
        if (StrUtil.isBlank(merchantUsers.getExamineState())) {
            merchantUsers.setExamineState("已通过");
        }
        merchantUsersService.save(merchantUsers);
        return Result.success("申请成功", null);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) String examineState,
                       HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        Page<MerchantUsers> p = new Page<>(page, size);
        QueryWrapper<MerchantUsers> qw = new QueryWrapper<>();
        if (StrUtil.isNotBlank(examineState)) {
            qw.eq("examine_state", examineState);
        }
        qw.orderByDesc("create_time");
        merchantUsersService.page(p, qw);
        return Result.page(p.getRecords(), p.getTotal());
    }

    @PutMapping("/review")
    public Result review(@RequestBody MerchantUsers merchantUsers, HttpServletRequest request) {
        Result auth = AuthSupport.requireAdmin(request);
        if (auth != null) return auth;
        merchantUsersService.updateById(merchantUsers);
        return Result.success("审核完成", null);
    }

    @GetMapping("/my")
    public Result myMerchant(HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        MerchantUsers merchant = merchantUsersService.lambdaQuery().eq(MerchantUsers::getUserId, userId).one();
        return Result.success(merchant);
    }

    @PostMapping("/service/add")
    public Result addService(@RequestBody MerchantCustomerService service, HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        if (service.getGoodsId() == null) {
            return Result.error("商品不存在");
        }
        Goods goods = goodsService.getById(service.getGoodsId());
        if (goods == null) {
            return Result.error("商品不存在");
        }
        MerchantUsers merchant = merchantUsersService.lambdaQuery().eq(MerchantUsers::getUserId, goods.getUserId()).one();
        if (merchant == null) {
            return Result.error("当前商品未绑定商家");
        }
        if (StrUtil.isBlank(service.getMessageContent())) {
            return Result.error("消息内容不能为空");
        }

        MerchantCustomerService message = new MerchantCustomerService();
        message.setUserId(userId);
        message.setGoodsId(goods.getGoodsId());
        message.setGoodsTitle(StrUtil.blankToDefault(service.getGoodsTitle(), goods.getTitle()));
        message.setMerchantUsers(merchant.getMerchantUsersId());
        message.setSenderType(SENDER_USER);
        message.setMessageContent(service.getMessageContent().trim());
        message.setReadState(READ_UNREAD);
        message.setSessionId(buildSessionId(goods.getGoodsId(), merchant.getMerchantUsersId(), userId));
        merchantCustomerServiceService.save(message);
        return Result.success("发送成功", message);
    }

    @GetMapping("/service/my-sessions")
    public Result mySessions(HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        QueryWrapper<MerchantCustomerService> qw = new QueryWrapper<>();
        qw.eq("user_id", userId)
                .isNotNull("session_id")
                .orderByDesc("create_time");
        List<MerchantCustomerService> messages = merchantCustomerServiceService.list(qw);
        return Result.success(buildSessionList(messages, SENDER_USER));
    }

    @GetMapping("/service/messages/{sessionId}")
    public Result myMessages(@PathVariable String sessionId, HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        QueryWrapper<MerchantCustomerService> qw = new QueryWrapper<>();
        qw.eq("session_id", sessionId)
                .eq("user_id", userId)
                .orderByAsc("create_time");
        List<MerchantCustomerService> list = merchantCustomerServiceService.list(qw);
        markMessagesRead(sessionId, SENDER_MERCHANT);
        return Result.success(list);
    }

    @GetMapping("/service/list")
    public Result serviceList(HttpServletRequest request) {
        MerchantUsers merchant = requireCurrentMerchant(request);
        if (merchant == null) {
            return Result.error(403, "当前账号未绑定商家");
        }
        QueryWrapper<MerchantCustomerService> qw = new QueryWrapper<>();
        qw.eq("merchant_users", merchant.getMerchantUsersId())
                .isNotNull("session_id")
                .orderByDesc("create_time");
        List<MerchantCustomerService> messages = merchantCustomerServiceService.list(qw);
        return Result.success(buildSessionList(messages, SENDER_MERCHANT));
    }

    @GetMapping("/service/detail/{sessionId}")
    public Result serviceDetail(@PathVariable String sessionId, HttpServletRequest request) {
        MerchantUsers merchant = requireCurrentMerchant(request);
        if (merchant == null) {
            return Result.error(403, "当前账号未绑定商家");
        }
        QueryWrapper<MerchantCustomerService> qw = new QueryWrapper<>();
        qw.eq("session_id", sessionId)
                .eq("merchant_users", merchant.getMerchantUsersId())
                .orderByAsc("create_time");
        List<MerchantCustomerService> list = merchantCustomerServiceService.list(qw);
        markMessagesRead(sessionId, SENDER_USER);
        return Result.success(list);
    }

    @PutMapping("/service/reply")
    public Result replyService(@RequestBody MerchantCustomerService service, HttpServletRequest request) {
        MerchantUsers merchant = requireCurrentMerchant(request);
        if (merchant == null) {
            return Result.error(403, "当前账号未绑定商家");
        }
        if (StrUtil.isBlank(service.getSessionId()) || StrUtil.isBlank(service.getMessageContent())) {
            return Result.error("参数不完整");
        }
        MerchantCustomerService first = merchantCustomerServiceService.lambdaQuery()
                .eq(MerchantCustomerService::getSessionId, service.getSessionId())
                .eq(MerchantCustomerService::getMerchantUsers, merchant.getMerchantUsersId())
                .last("limit 1")
                .one();
        if (first == null) {
            return Result.error("会话不存在");
        }
        MerchantCustomerService reply = new MerchantCustomerService();
        reply.setSessionId(first.getSessionId());
        reply.setUserId(first.getUserId());
        reply.setGoodsId(first.getGoodsId());
        reply.setGoodsTitle(first.getGoodsTitle());
        reply.setMerchantUsers(first.getMerchantUsers());
        reply.setSenderType(SENDER_MERCHANT);
        reply.setMessageContent(service.getMessageContent().trim());
        reply.setReadState(READ_UNREAD);
        merchantCustomerServiceService.save(reply);
        return Result.success("回复成功", reply);
    }

    @DeleteMapping("/service/delete/{id}")
    public Result deleteService(@PathVariable Integer id, HttpServletRequest request) {
        MerchantUsers merchant = requireCurrentMerchant(request);
        if (merchant == null) {
            return Result.error(403, "当前账号未绑定商家");
        }
        MerchantCustomerService db = merchantCustomerServiceService.getById(id);
        if (db == null || !merchant.getMerchantUsersId().equals(db.getMerchantUsers())) {
            return Result.error("消息不存在");
        }
        merchantCustomerServiceService.removeById(id);
        return Result.success("删除成功", null);
    }

    private MerchantUsers requireCurrentMerchant(HttpServletRequest request) {
        Integer userId = AuthSupport.getUserId(request);
        return merchantUsersService.lambdaQuery().eq(MerchantUsers::getUserId, userId).one();
    }

    private String buildSessionId(Integer goodsId, Integer merchantUsersId, Integer customerUserId) {
        return goodsId + "_" + merchantUsersId + "_" + customerUserId;
    }

    private List<Map<String, Object>> buildSessionList(List<MerchantCustomerService> messages, String currentSenderType) {
        Map<String, Map<String, Object>> sessionMap = new LinkedHashMap<>();
        for (MerchantCustomerService item : messages) {
            if (StrUtil.isBlank(item.getSessionId())) {
                continue;
            }
            Map<String, Object> session = sessionMap.get(item.getSessionId());
            if (session == null) {
                session = new LinkedHashMap<>();
                session.put("sessionId", item.getSessionId());
                session.put("goodsId", item.getGoodsId());
                session.put("goodsTitle", item.getGoodsTitle());
                session.put("userId", item.getUserId());
                session.put("merchantUsers", item.getMerchantUsers());
                session.put("lastMessage", item.getMessageContent());
                session.put("lastSenderType", item.getSenderType());
                session.put("lastTime", item.getCreateTime());
                session.put("unreadCount", 0);
                sessionMap.put(item.getSessionId(), session);
            }
            if (!currentSenderType.equals(item.getSenderType()) && READ_UNREAD.equals(item.getReadState())) {
                Integer unreadCount = (Integer) session.get("unreadCount");
                session.put("unreadCount", unreadCount + 1);
            }
        }
        return new ArrayList<>(sessionMap.values());
    }

    private void markMessagesRead(String sessionId, String senderType) {
        List<MerchantCustomerService> unreadList = merchantCustomerServiceService.lambdaQuery()
                .eq(MerchantCustomerService::getSessionId, sessionId)
                .eq(MerchantCustomerService::getSenderType, senderType)
                .eq(MerchantCustomerService::getReadState, READ_UNREAD)
                .list();
        for (MerchantCustomerService item : unreadList) {
            MerchantCustomerService update = new MerchantCustomerService();
            update.setMerchantCustomerServiceId(item.getMerchantCustomerServiceId());
            update.setReadState(READ_READ);
            merchantCustomerServiceService.updateById(update);
        }
    }
}
