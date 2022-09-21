package com.taoyes3.credit.sys.service;

import com.taoyes3.credit.sys.model.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author taoyes3
* @description 针对表【sys_user(系统用户)】的数据库操作Service
* @createDate 2022-09-19 17:10:46
*/
public interface SysUserService extends IService<SysUser> {

    void saveUserAndUserRole(SysUser sysUser);

    void updateUserAndUserRole(SysUser sysUser);
}
