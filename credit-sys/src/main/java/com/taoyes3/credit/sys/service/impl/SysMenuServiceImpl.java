package com.taoyes3.credit.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taoyes3.credit.common.exception.CreditBindException;
import com.taoyes3.credit.sys.constant.MenuType;
import com.taoyes3.credit.sys.dao.SysMenuMapper;
import com.taoyes3.credit.sys.model.SysMenu;
import com.taoyes3.credit.sys.service.SysMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author taoyes3
 * @date 2022/9/15 10:33
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> listMenuAndBtn() {
        return sysMenuMapper.listMenuAndBtn();
    }

    @Override
    public void verifyForm(SysMenu sysMenu) {
        if (sysMenu.getType() == MenuType.MENU.getValue()) {
            if (StrUtil.isBlank(sysMenu.getUrl())) {
                throw new CreditBindException("菜单URL不能为空");
            }
        }
        if (Objects.equals(sysMenu.getId(),sysMenu.getParentId())) {
            throw new CreditBindException("自己不能是自己的上级");
        }
        //上级菜单类型
        int parentType = MenuType.CATALOG.getValue();
        if (sysMenu.getParentId() != 0) {
            SysMenu parentMenu = this.getById(sysMenu.getParentId());
            parentType = parentMenu.getType();
        }
        //目录、菜单
        if (sysMenu.getType() == MenuType.CATALOG.getValue() || sysMenu.getType() == MenuType.MENU.getValue()) {
            if (parentType != MenuType.CATALOG.getValue()) {
                throw new CreditBindException("上级菜单只能为目录类型");
            }
            return;
        }
        //按钮
        if (sysMenu.getType() == MenuType.BUTTON.getValue()) {
            if (parentType != MenuType.MENU.getValue()) {
                throw new CreditBindException("上级菜单只能为菜单类型");
            }
        }
    }

    @Override
    public List<SysMenu> listChildrenMenuByParentId(Long parentId) {
        return sysMenuMapper.listChildrenMenuByParentId(parentId);
    }

    @Override
    public void deleteMenuAndRoleMenu(Long id) {
        //删除菜单
        this.removeById(id);
        // TODO: 2022/9/15 删除菜单与角色关联
    }
}
