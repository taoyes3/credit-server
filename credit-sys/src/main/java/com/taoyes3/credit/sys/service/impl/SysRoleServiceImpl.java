package com.taoyes3.credit.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taoyes3.credit.sys.dao.SysRoleMenuMapper;
import com.taoyes3.credit.sys.model.SysRole;
import com.taoyes3.credit.sys.model.SysRoleMenu;
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

    @Override
    @Transactional
    public void saveRoleAndRoleMenu(SysRole sysRole) {
        log.info("sysRole对象：{}", sysRole);
        sysRole.setCreateTime(new Date());
        this.save(sysRole);
        //角色是否配置菜单权限
        if (CollectionUtil.isEmpty(sysRole.getMenuIdList())) {
            return;
        }
        //保存角色与菜单关系
        sysRoleMenuMapper.insertRoleAndRoleMenu(sysRole.getId(), sysRole.getMenuIdList());
    }

    @Override
    @Transactional
    public void updateRoleAndRoleMenu(SysRole sysRole) {
        this.updateById(sysRole);
        //先删除原分配的菜单
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, sysRole.getId());
        sysRoleMenuMapper.delete(wrapper);
        //角色是否配置菜单
        if (CollectionUtil.isEmpty(sysRole.getMenuIdList())) {
            return;
        }
        //保存角色与菜单关系
        sysRoleMenuMapper.insertRoleAndRoleMenu(sysRole.getId(), sysRole.getMenuIdList());
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        //删除角色
        this.removeByIds(ids);
        //删除角色与菜单关联
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<SysRoleMenu>().in(SysRoleMenu::getRoleId, ids);
        sysRoleMenuMapper.delete(sysRoleMenuLambdaQueryWrapper);
        // TODO: 2022/9/17 删除角色与用户关联 
    }
}




