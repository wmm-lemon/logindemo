package com.wmm.login.logindemo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

/**
 * Created by wmm on 2019/7/29.
 */
@Entity
@Table(name = "acas_user")
@Data
public class SysUser {

    @Id
    private Integer id;
    private String username;
    private String password;
    private String salt;

    /**
     * 用户密码加密，盐值为 ：私盐+公盐
     * @param  password 密码
     * @param  salt 私盐
     * @return  MD5加密字符串
     */
    public static String encryptPassword(String password,String salt){
        password = md5Hex(salt+password);

        return password.toUpperCase();
    }
}
