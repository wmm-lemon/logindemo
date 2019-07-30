package com.wmm.login.logindemo.config;

import com.wmm.login.logindemo.config.auth.CustomAuthentication;
import com.wmm.login.logindemo.config.handler.LoginFailureHandler;
import com.wmm.login.logindemo.config.handler.LoginSuccessHandler;
import com.wmm.login.logindemo.filter.LoginAuthFilter;
import com.wmm.login.logindemo.filter.RequstAuthFilter;
import com.wmm.login.logindemo.service.SysUserService;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wmm on 2019/7/24.
 */
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SysUserService userService;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private HttpServletRequest request;

    // 设置 HTTP 验证规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .loginPage("/login")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/login","/logout",
                        "/img/**","/js/**",
                        "/v2/**", "/configuration/**", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**"//swagger页面不拦截
                )
                .permitAll()
                .anyRequest().authenticated()  // 所有请求需要身份认证
                .and().headers().addHeaderWriter((request, response) -> {
                    response.setHeader("Access-Control-Allow-Origin", "*");
                    response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
                    response.setHeader("Access-Control-Allow-Headers", "Authorization");
                    if ("OPTIONS".equals(request.getMethod().toUpperCase())) {
                        response.setStatus(HttpStatus.SC_NO_CONTENT);
                    }
                })
                .and()
                .addFilter(new LoginAuthFilter(authenticationManager()))
                .addFilter(new RequstAuthFilter(authenticationManager()))
                .logout() // 默认注销行为为logout，可以通过下面的方式来修改
                .logoutUrl("/logout")
                .permitAll();// 设置注销成功后跳转页面，默认是跳转到登录页面;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new CustomAuthentication(userService, request));
    }

}
