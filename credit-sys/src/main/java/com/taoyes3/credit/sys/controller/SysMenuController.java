package com.taoyes3.credit.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.taoyes3.credit.common.exception.CreditBindException;
import com.taoyes3.credit.sys.constant.MenuType;
import com.taoyes3.credit.sys.model.SysMenu;
import com.taoyes3.credit.sys.service.SysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author taoyes3
 * @date 2022/9/15 10:31
 */
@RestController
@Slf4j
@RequestMapping("/sys/menu")
public class SysMenuController {
    @Resource
    private SysMenuService sysMenuService;

    @GetMapping
    public ResponseEntity<List<SysMenu>> index() {
        List<SysMenu> sysMenuList =  sysMenuService.listMenuAndBtn();
        return ResponseEntity.ok(sysMenuList);
    }

    @PostMapping
    public void store(@RequestBody SysMenu sysMenu) {
        //数据校验
        verifyForm(sysMenu);
        sysMenuService.save(sysMenu);
    }

    private void verifyForm(SysMenu sysMenu) {
        if (sysMenu.getType() == MenuType.MENU.getValue()) {
            if (StrUtil.isBlank(sysMenu.getUrl())) {
                throw new CreditBindException("菜单URL不能为空");
            }
        }
    }
}
