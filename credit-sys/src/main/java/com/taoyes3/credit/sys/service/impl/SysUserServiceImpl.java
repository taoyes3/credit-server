package com.taoyes3.credit.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taoyes3.credit.sys.dao.SysRoleMapper;
import com.taoyes3.credit.sys.dao.SysUserRoleMapper;
import com.taoyes3.credit.sys.model.SysUser;
import com.taoyes3.credit.sys.model.SysUserRole;
import com.taoyes3.credit.sys.service.SysUserService;
import com.taoyes3.credit.sys.dao.SysUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
* @author taoyes3
* @description 针对表【sys_user(系统用户)】的数据库操作Service实现
* @createDate 2022-09-19 17:10:46
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    
    @Override
    @Transactional
    public void saveUserAndUserRole(SysUser sysUser) {
        sysUser.setCreateTime(new Date());
        this.save(sysUser);
        if (CollUtil.isEmpty(sysUser.getRoleIdList())) {
            return;
        }
        //保存用户与角色关系
        sysUserRoleMapper.insertUserAndUserRole(sysUser.getId(), sysUser.getRoleIdList());
    }

    @Override
    @Transactional
    public void updateUserAndUserRole(SysUser sysUser) {
        // 更新用户
        this.updateById(sysUser);
        // 先删除用户与角色关系
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, sysUser.getId()));
        
        if (CollUtil.isEmpty(sysUser.getRoleIdList())) {
            return;
        }
        // 保存用户与角色关系
        sysUserRoleMapper.insertUserAndUserRole(sysUser.getId(), sysUser.getRoleIdList());
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 删除用户
        this.removeByIds(ids);
        // 删除用户和角色关联
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getUserId, ids));
    }
}




