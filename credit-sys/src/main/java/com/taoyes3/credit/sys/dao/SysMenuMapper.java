package com.taoyes3.credit.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taoyes3.credit.sys.model.SysMenu;

import java.util.List;

/**
 * @author taoyes3
 * @date 2022/9/15 10:34
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> listMenuAndBtn();

    List<SysMenu> listChildrenMenuByParentId(Long parentId);
}
