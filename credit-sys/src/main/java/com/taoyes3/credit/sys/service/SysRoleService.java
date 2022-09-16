package com.taoyes3.credit.sys.service;

import com.taoyes3.credit.sys.model.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author taoyes3
 * @description 针对表【sys_role(角色)】的数据库操作Service
 * @createDate 2022-09-16 16:16:23
 */
public interface SysRoleService extends IService<SysRole> {

    void saveRoleAndRoleMenu(SysRole sysRole);
}
