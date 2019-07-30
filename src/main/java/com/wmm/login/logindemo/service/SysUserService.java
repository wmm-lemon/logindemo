package com.wmm.login.logindemo.service;

import com.wmm.login.logindemo.bean.ResultBean;
import com.wmm.login.logindemo.entity.SysUser;
import com.wmm.login.logindemo.repository.SysUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wmm on 2019/7/29.
 */
@Service
@Slf4j
public class SysUserService {

    @Autowired
    SysUserRepository userRepository;

    /**
     * 登录
     * @return
     */
    public ResultBean login(SysUser user){
        try{
            SysUser userByName = userRepository.findByUsername(user.getUsername());
            if (null==userByName){
                return new ResultBean().failed("账号错误！");
            }else {
                String password = user.encryptPassword(user.getPassword(),userByName.getSalt());
//                user.setPassword();
                SysUser acasUser = userRepository.findByUsernameAndPassword(user.getUsername(),password);
                if (null!=acasUser){
                    return new ResultBean().success("成功").setData(acasUser);
                }else{
                    return new ResultBean().failed("密码错误！");
                }
            }
        }catch(Exception ex){
            log.error("login错误:"+ex.toString());
            return new ResultBean().error(ex);
        }
    }

}
