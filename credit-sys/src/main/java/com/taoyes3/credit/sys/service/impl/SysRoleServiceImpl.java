package com.taoyes3.credit.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taoyes3.credit.sys.dao.SysRoleMenuMapper;
import com.taoyes3.credit.sys.model.SysRole;
import com.taoyes3.credit.sys.service.SysRoleService;
import com.taoyes3.credit.sys.dao.SysRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author taoyes3
 * @description 针对表【sys_role(角色)】的数据库操作Service实现
 * @createDate 2022-09-16 16:16:23
 */
@Service
@Slf4j
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public void saveRoleAndRoleMenu(SysRole sysRole) {
        log.info("sysRole对象：{}", sysRole);
        sysRole.setCreateTime(new Date());
        this.save(sysRole);
        //角色是否配置菜单权限
        if (CollectionUtil.isEmpty(sysRole.getMenuIdList())) {
            return;
        }
        //保存角色与菜单关系
        sysRoleMenuMapper.insertRoleAndMenu(sysRole.getId(), sysRole.getMenuIdList());
    }
}




