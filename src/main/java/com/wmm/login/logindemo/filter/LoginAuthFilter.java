package com.wmm.login.logindemo.filter;

import com.alibaba.fastjson.JSON;
import com.wmm.login.logindemo.bean.ResultBean;
import com.wmm.login.logindemo.util.ConstUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;

/**
 * 验证用户名密码正确后，生成一个token，并将token返回给客户端
 * 该类继承自UsernamePasswordAuthenticationFilter，重写了其中的2个方法
 * attemptAuthentication ：接收并解析用户凭证。
 * successfulAuthentication ：用户成功登录后，这个方法会被调用，我们在这个方法里生成token。
 *
 * @author wwg on 2017/9/12.
 */
@Slf4j
public class LoginAuthFilter extends UsernamePasswordAuthenticationFilter implements Filter {

    private AuthenticationManager authenticationManager;

    public LoginAuthFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // 接收并解析用户凭证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        if (!req.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("非法请求！");
        }
        try {
            res.setHeader("Access-Control-Allow-Origin", "*");
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            log.info("用户：" + username + " 开始登入！");
            if (username != null && password != null) {
                return authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                username,
                                password,
                                new ArrayList<>())
                );
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 用户成功登录后，这个方法会被调用，我们在这个方法里生成token
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain,
                                            Authentication authentication) {
        response.setContentType("application/json;charset=UTF-8");
        try {
            ResultBean resultBean = new ResultBean();
            String token;
            if (Integer.parseInt(authentication.getName()) > 0) {
                token = Jwts.builder()
                        .setSubject(authentication.getName())
                        .setExpiration(new Date(System.currentTimeMillis() + ConstUtil.JWTEXPIRATION))
                        .signWith(SignatureAlgorithm.HS512, ConstUtil.SIGNING_KEY) //采用什么算法是可以自己选择的，不一定非要采用HS512
                        .compact();
                response.addHeader("Authorization", token);
                resultBean.success("登入成功！");
                log.info("用户Id：" + authentication.getName() + " 登入成功！");
            }
            if (authentication.getPrincipal().equals("0")) {
                resultBean.failed("用户名或密码错误！");
            }
            response.getWriter().write(JSON.toJSONString(resultBean));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
