package com.taoyes3.credit.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taoyes3.credit.sys.model.SysRoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author taoyes3
* @description 针对表【sys_role_menu(角色与菜单对应关系)】的数据库操作Mapper
* @createDate 2022-09-16 17:24:13
* @Entity com.taoyes3.credit.sys.model.SysRoleMenu
*/
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    void insertRoleAndRoleMenu(@Param("roleId") Long roleId, @Param("menuIdList") List<Long> menuIdList);
}




