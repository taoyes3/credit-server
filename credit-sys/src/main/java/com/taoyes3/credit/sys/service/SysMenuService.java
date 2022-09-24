package com.taoyes3.credit.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taoyes3.credit.sys.model.SysMenu;

import java.util.List;

/**
 * @author taoyes3
 * @date 2022/9/15 10:32
 */
public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> listMenuAndBtn();

    void verifyForm(SysMenu sysMenu);

    /**
     * 获取子菜单
     * @param parentId
     * @return
     */
    List<SysMenu> listChildrenMenuByParentId(Long parentId);

    /**
     * 删除 菜单，与角色菜单之间的关系
     * @param id
     */
    void deleteMenuAndRoleMenu(Long id);

    /**
     * 获取简单的menu tree 用于在ele-ui tree中显示，根据orderNum排序
     * @return
     */
    List<SysMenu> listSimpleMenuNoButton();

    List<String> queryAllPerms(Long userId);
}
