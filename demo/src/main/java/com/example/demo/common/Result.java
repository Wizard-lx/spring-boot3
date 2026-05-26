package com.example.demo.common;

import lombok.Data;
import java.io.Serializable;

@Data
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;
    //状态码
    private int code;
    //提示信息 message
    private String msg;
    //返回属性类型
    private Object data;

    // 直接返回成功结果
    public static Result success(Object data) {
        return success(200, "操作成功", data);
    }

    // 自定义返回成功结果
    public static Result success(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    //不带结果直接返回成功
    public static Result success(){
        Result r = new Result();
        r.setCode(200);
        r.setMsg("操作成功");
        return r;
    }

    // 直接返回失败信息
    public static Result error() {
        return new Result(400, "操作失败", null);
    }

    //带参数返回失败信息
    public static Result error(String msg) {
        return new Result(400, msg, null);
    }

    // 无参构造
    public Result() {
    }

    // 全参构造
    public Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}