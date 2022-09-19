package com.taoyes3.credit.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taoyes3.credit.sys.model.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author taoyes3
* @description 针对表【sys_user_role(用户与角色对应关系)】的数据库操作Mapper
* @createDate 2022-09-19 17:26:09
* @Entity com.taoyes3.credit.sys.model.SysUserRole
*/
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    void insertUserAndUserRole(@Param("userId") Long userId, @Param("roleIdList") List<Long> roleIdList);
}




