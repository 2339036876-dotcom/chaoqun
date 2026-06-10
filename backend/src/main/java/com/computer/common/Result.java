package com.computer.common;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class Result {
    private int code;
    private String msg;
    private Object data;

    public static Result success() {
        Result r = new Result();
        r.setCode(0);
        r.setMsg("success");
        return r;
    }

    public static Result success(Object data) {
        Result r = success();
        r.setData(data);
        return r;
    }

    public static Result success(String msg, Object data) {
        Result r = success();
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static Result error(String msg) {
        Result r = new Result();
        r.setCode(1);
        r.setMsg(msg);
        return r;
    }

    public static Result error(int code, String msg) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    public static Result page(Object list, long total) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("total", total);
        return success(map);
    }
}
