package com.wmm.login.logindemo.repository;

import com.wmm.login.logindemo.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wmm on 2019/7/29.
 */
public interface SysUserRepository extends JpaRepository<SysUser,Integer> {

    SysUser findByUsername(String username);

    SysUser findByUsernameAndPassword(String username,String password);

}
