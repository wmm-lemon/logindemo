package com.wmm.login.logindemo.filter;


import com.alibaba.fastjson.JSON;
import com.wmm.login.logindemo.bean.ResultBean;
import com.wmm.login.logindemo.exception.LoginException;
import com.wmm.login.logindemo.util.ConstUtil;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * token的校验
 * 该类继承自BasicAuthenticationFilter，在doFilterInternal方法中，
 * 从http头的Authorization 项读取token数据，然后用Jwts包提供的方法校验token的合法性。
 * 如果校验通过，就认为这是一个取得授权的合法请求
 *
 * @author wwg on 2017/9/13.
 */
public class RequstAuthFilter extends BasicAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequstAuthFilter.class);

    public RequstAuthFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request, response);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getHeader("Authorization");
        if (token != null) {
            String user;
            try {
                user = Jwts.parser()
                        .setSigningKey(ConstUtil.SIGNING_KEY)
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();
                if (user != null) {
                    return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                }
            } catch (ExpiredJwtException e) {
                response.getWriter().write(JSON.toJSONString(new ResultBean().failed("授权已过期！").setCode(-999)));
            } catch (UnsupportedJwtException e) {
                logger.error("Token格式错误: {} " + e);
                throw new LoginException("Token格式错误");
            } catch (MalformedJwtException e) {
                logger.error("Token没有被正确构造: {} " + e);
                throw new LoginException("Token没有被正确构造");
            } catch (SignatureException e) {
                logger.error("签名失败: {} " + e);
                throw new LoginException("签名失败");
            } catch (IllegalArgumentException e) {
                logger.error("非法参数异常: {} " + e);
                throw new LoginException("非法参数异常");
            }
        }
        return null;
    }

}
