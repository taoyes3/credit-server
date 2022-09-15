package com.taoyes3.credit.sys.controller;

import com.taoyes3.credit.common.exception.CreditBindException;
import com.taoyes3.credit.sys.constant.Constant;
import com.taoyes3.credit.sys.model.SysMenu;
import com.taoyes3.credit.sys.service.SysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
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
    public ResponseEntity<Object> store(@Valid @RequestBody SysMenu sysMenu) {
        //数据校验
        sysMenuService.verifyForm(sysMenu);
        sysMenuService.save(sysMenu);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Object> update(@Valid @RequestBody SysMenu sysMenu) {
        //数据校验
        sysMenuService.verifyForm(sysMenu);
        sysMenuService.updateById(sysMenu);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        if (id <= Constant.SYS_MENU_MAX_ID) {
            throw new CreditBindException("系统菜单，不能删除");
        }
        //判断是否有子菜单或按钮
        List<SysMenu> sysMenuList = sysMenuService.listChildrenMenuByParentId(id);
        if (!sysMenuList.isEmpty()) {
            throw new CreditBindException("请先删除子菜单或按钮");
        }
        sysMenuService.deleteMenuAndRoleMenu(id);

        return ResponseEntity.ok().build();
    }

    /**
     * 所有菜单列表(用于新建、修改角色时 获取菜单的信息)
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<SysMenu>> list() {
        List<SysMenu> sysMenuList = sysMenuService.listSimpleMenuNoButton();
        return ResponseEntity.ok(sysMenuList);
    }


}
