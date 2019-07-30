package com.wmm.login.logindemo.controller;

import com.wmm.login.logindemo.bean.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wmm on 2019/7/29.
 */
@Api(value = "LoginController",tags = "登录接口")
@RestController
public class LoginController {

    @ApiOperation(value = "登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",value = "用户名",required = true,paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "password",value = "密码",required = true,paramType = "query",dataType = "String")
    })
    @PostMapping("login")
    public ResultBean login(String username, String password){
        return null;
    }

}
