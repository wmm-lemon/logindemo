package com.wmm.login.logindemo.controller;

import com.wmm.login.logindemo.bean.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wmm on 2019/7/29.
 */
@Api(value = "IndexController",tags = "首页接口")
@RestController
@RequestMapping("index")
public class IndexController {
    @ApiOperation(value = "首页接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "msg_cn",value = "返回值",required = true,paramType = "query",dataType = "String")
    })
    @GetMapping("index")
    public ResultBean index(String msg_cn){
        return new ResultBean().success(msg_cn);
    }
}
