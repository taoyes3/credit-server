package com.taoyes3.credit.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taoyes3.credit.common.util.PageParam;
import com.taoyes3.credit.sys.model.SysRole;
import com.taoyes3.credit.sys.model.SysRoleMenu;
import com.taoyes3.credit.sys.service.SysRoleMenuService;
import com.taoyes3.credit.sys.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author taoyes3
 * @date 2022/9/16 16:15
 */
@RestController
@Slf4j
@RequestMapping("/sys/role")
public class SysRoleController {
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @GetMapping
    public ResponseEntity<IPage<SysRole>> index(String name, PageParam<SysRole> pageParam) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(name), SysRole::getName, name);
        IPage<SysRole> sysRoles = sysRoleService.page(pageParam, wrapper);

        return ResponseEntity.ok(sysRoles);
    }

    @PostMapping
    public ResponseEntity<Object> store(@Valid @RequestBody SysRole sysRole) {
        sysRoleService.saveRoleAndRoleMenu(sysRole);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Object> update(@Valid @RequestBody SysRole sysRole) {
        sysRoleService.updateRoleAndRoleMenu(sysRole);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody List<Long> ids) {
        sysRoleService.deleteBatch(ids);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/info/{id}")
    public ResponseEntity<SysRole> info(@PathVariable Long id) {
        SysRole sysRole = sysRoleService.getById(id);
        //查询角色对应的菜单
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id);
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuService.list(wrapper);
        List<Long> menuIdList = sysRoleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
        //log.info("sysRoleMenus的值：{}", sysRoleMenus);
        //log.info("menuIdList的值：{}", menuIdList);
        sysRole.setMenuIdList(menuIdList);
        
        return ResponseEntity.ok(sysRole);
    }
}
