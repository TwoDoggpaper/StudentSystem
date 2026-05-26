package com.stbu.utils; // 这个包名可以按你项目改

/**
 * 通用返回结果封装类
 */
public class RestResponse<T> {
    // 状态码：200成功，500失败
    private int code;
    // 提示信息
    private String msg;
    // 返回数据
    private T data;

    // 成功
    public static <T> RestResponse<T> ok(T data) {
        RestResponse<T> response = new RestResponse<>();
        response.setCode(200);
        response.setMsg("操作成功");
        response.setData(data);
        return response;
    }

    // 成功（无数据）
    public static <T> RestResponse<T> ok() {
        return ok(null);
    }

    // 失败
    public static <T> RestResponse<T> fail(String msg) {
        RestResponse<T> response = new RestResponse<>();
        response.setCode(500);
        response.setMsg(msg);
        response.setData(null);
        return response;
    }

    // getter & setter
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}