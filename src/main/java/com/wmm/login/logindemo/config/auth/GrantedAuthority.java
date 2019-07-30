package com.wmm.login.logindemo.config.auth;


/**
 * 权限类型，负责存储权限和角色
 *
 * @author wwg on 2017/9/12.
 */
public class GrantedAuthority implements org.springframework.security.core.GrantedAuthority {

    private String authority;

    public GrantedAuthority(String authority) {
        this.authority = authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
