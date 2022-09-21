package com.taoyes3.credit.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taoyes3.credit.sys.dao.SysRoleMenuMapper;
import com.taoyes3.credit.sys.dao.SysUserRoleMapper;
import com.taoyes3.credit.sys.model.SysRole;
import com.taoyes3.credit.sys.model.SysRoleMenu;
import com.taoyes3.credit.sys.model.SysUserRole;
import com.taoyes3.credit.sys.service.SysRoleService;
import com.taoyes3.credit.sys.dao.SysRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    @Transactional
    public void saveRoleAndRoleMenu(SysRole sysRole) {
        log.info("sysRole对象：{}", sysRole);
        sysRole.setCreateTime(new Date());
        this.save(sysRole);
        // 角色是否配置菜单权限
        if (CollectionUtil.isEmpty(sysRole.getMenuIdList())) {
            return;
        }
        // 保存角色与菜单关系
        sysRoleMenuMapper.insertRoleAndRoleMenu(sysRole.getId(), sysRole.getMenuIdList());
    }

    @Override
    @Transactional
    public void updateRoleAndRoleMenu(SysRole sysRole) {
        this.updateById(sysRole);
        // 先删除原分配的菜单
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, sysRole.getId()));
        // 角色是否配置菜单
        if (CollectionUtil.isEmpty(sysRole.getMenuIdList())) {
            return;
        }
        // 保存角色与菜单关系
        sysRoleMenuMapper.insertRoleAndRoleMenu(sysRole.getId(), sysRole.getMenuIdList());
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 删除角色
        this.removeByIds(ids);
        // 删除角色与菜单关联
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().in(SysRoleMenu::getRoleId, ids));
        // 删除角色与用户关联 
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getRoleId, ids));
    }
}




