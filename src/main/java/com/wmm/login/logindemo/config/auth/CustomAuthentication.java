package com.wmm.login.logindemo.config.auth;

import com.wmm.login.logindemo.bean.ResultBean;
import com.wmm.login.logindemo.entity.SysUser;
import com.wmm.login.logindemo.service.SysUserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

/**
 * 自定义身份认证验证组件
 *
 * @author wwg on 2017/9/12.
 */
public class CustomAuthentication implements AuthenticationProvider {


    private SysUserService userService;

    private HttpServletRequest request;

    public CustomAuthentication(SysUserService userService, HttpServletRequest request) {
        this.userService = userService;
        this.request = request;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取认证的用户名 & 密码
        SysUser user = new SysUser();
        String username = authentication.getName();
        String password;
        password = authentication.getCredentials().toString();
        user.setUsername(username);
        user.setPassword(password);
        ResultBean login = userService.login(user);
        // 认证逻辑
        if (login.getCode() == 1) { //如果用户存在
            Map data = login.getDataParseEntity(Map.class);
            // 这里设置权限和角色
            ArrayList<org.springframework.security.core.GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new GrantedAuthority("ROLE_ADMIN"));
            authorities.add(new GrantedAuthority("AUTH_WRITE"));
            // 生成令牌 这里令牌里面存入了:name,password,authorities, 当然你也可以放其他内容
            Authentication auth = new UsernamePasswordAuthenticationToken(data.get("id"), password, authorities);
            return auth;
        } else {
            return new UsernamePasswordAuthenticationToken("0", "用户名或密码错误！");
        }
    }

    /**
     * 是否可以提供输入类型的认证服务
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
