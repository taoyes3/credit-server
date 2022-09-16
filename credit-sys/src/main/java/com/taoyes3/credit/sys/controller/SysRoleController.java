package com.taoyes3.credit.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taoyes3.credit.common.util.PageParam;
import com.taoyes3.credit.sys.model.SysRole;
import com.taoyes3.credit.sys.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    @GetMapping
    public ResponseEntity<IPage<SysRole>> index(String name, PageParam<SysRole> pageParam) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(name), SysRole::getName, name);
        IPage<SysRole> sysRoles = sysRoleService.page(pageParam, wrapper);

        return ResponseEntity.ok(sysRoles);
    }
}
