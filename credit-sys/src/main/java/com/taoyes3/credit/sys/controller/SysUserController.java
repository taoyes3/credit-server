package com.taoyes3.credit.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taoyes3.credit.common.exception.CreditBindException;
import com.taoyes3.credit.common.util.PageParam;
import com.taoyes3.credit.sys.constant.Constant;
import com.taoyes3.credit.sys.model.SysUser;
import com.taoyes3.credit.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @author taoyes3
 * @date 2022/9/19 17:07
 */
@Slf4j
@RestController
@RequestMapping("/sys/user")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;
    
    @GetMapping
    public ResponseEntity<PageParam<SysUser>> index(String username, PageParam<SysUser> pageParam) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(username), SysUser::getUsername, username);
        PageParam<SysUser> sysUsers = sysUserService.page(pageParam, wrapper);
        return ResponseEntity.ok(sysUsers);
    }
    
    @PostMapping
    public ResponseEntity<Object> store(@Valid @RequestBody SysUser sysUser) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, sysUser.getUsername());
        SysUser user = sysUserService.getOne(wrapper);
        if (user != null) {
            throw new CreditBindException("该用户已存在");
        }
        // TODO: 2022/9/19 密码加密 
        sysUserService.saveUserAndUserRole(sysUser);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping
    public ResponseEntity<Object> update(@Valid @RequestBody SysUser sysUser) {
        log.info("sysUser对象：{}", sysUser);
        SysUser userInfo = sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, sysUser.getUsername()));
        if (userInfo != null && !Objects.equals(userInfo.getId(), sysUser.getId())) {
            throw new CreditBindException("该用户已存在");
        }
        // TODO: 2022/9/21 密码加密
        String password = StrUtil.isBlank(sysUser.getPassword()) ? null : sysUser.getPassword();
        sysUser.setPassword(password);
        
        if (Objects.equals(1L, sysUser.getId()) && sysUser.getStatus() == 0) {
            throw new CreditBindException("admin用户不可以被禁用");
        }
        sysUserService.updateUserAndUserRole(sysUser);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody List<Long> ids) {
        if (ids.contains(Constant.SUPER_ADMIN_ID)) {
            throw new CreditBindException("系统管理员不能删除");
        }
        // TODO: 2022/9/21 当前用户不能删除 
        // sysUserService.deleteBatch(ids);
        return ResponseEntity.ok().build();
    }
}
