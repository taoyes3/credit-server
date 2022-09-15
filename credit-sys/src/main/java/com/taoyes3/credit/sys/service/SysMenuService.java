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
}
