package com.wmm.login.logindemo.config.handler;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wmm.login.logindemo.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wmm on 2019/7/24.
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    private static final String LOGIN_SUCCESS_RESULT = JSON.toJSONString(new ResultBean().success("登录成功!"));

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException {
        response.setContentType("application/json;charset=UTF-8"); // 响应类型
        response.getWriter().write(objectMapper.writeValueAsString(LOGIN_SUCCESS_RESULT));
    }


}
