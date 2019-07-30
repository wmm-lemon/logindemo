package com.wmm.login.logindemo.bean;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by wmm on 2019/7/8.
 */
@Slf4j
public class ResultBean {

    private int code = 1;
    private String msg = "Success !!";
    private String msg_cn = "操作成功！";
    private Object data = null;

    public ResultBean() {
    }

    public ResultBean(int code, String msg, String msg_cn, Object data) {
        this.code = code;
        this.msg = msg;
        this.msg_cn = msg_cn;
        this.data = data;
    }

    public ResultBean success(String msg_cn) {
        this.msg_cn = msg_cn;
        return this;
    }

    public ResultBean failed(String msg_cn) {
        this.code = 0;
        this.msg = "Error !!";
        this.msg_cn = msg_cn;
        if (null!=msg_cn && !"".equals(msg_cn)){
            log.info(msg_cn);
        }
        return this;
    }

    public ResultBean error(Exception e) {
        this.code = 0;
        this.msg = "Error !!";
        if (e != null) {
            this.msg_cn = "系统错误！错误信息：" + e.getMessage();
        }
        log.error(e.getMessage());
        return this;
    }

    public int getCode() {
        return code;
    }

    public ResultBean setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResultBean setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getMsg_cn() {
        return msg_cn;
    }

    public ResultBean setMsg_cn(String msg_cn) {
        this.msg_cn = msg_cn;
        return this;
    }

    public Object getData() {
        return data;
    }

    public <T> T getDataParseEntity(Class<T> clazz) {
        return JSON.parseObject(JSON.toJSONString(this.data), clazz);
    }

    public ResultBean setData(Object data) {
        this.data = data;
        return this;
    }
}
