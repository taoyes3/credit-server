package com.taoyes3.credit.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taoyes3.credit.sys.dao.SysMenuMapper;
import com.taoyes3.credit.sys.model.SysMenu;
import com.taoyes3.credit.sys.service.SysMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
}
