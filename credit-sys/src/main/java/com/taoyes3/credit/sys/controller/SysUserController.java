package com.taoyes3.credit.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.taoyes3.credit.common.exception.CreditBindException;
import com.taoyes3.credit.common.util.PageParam;
import com.taoyes3.credit.security.common.enums.SysTypeEnum;
import com.taoyes3.credit.security.common.manager.TokenManager;
import com.taoyes3.credit.security.common.util.AuthUserContext;
import com.taoyes3.credit.sys.constant.Constant;
import com.taoyes3.credit.sys.dto.UpdatePasswordDto;
import com.taoyes3.credit.sys.model.SysUser;
import com.taoyes3.credit.sys.model.SysUserRole;
import com.taoyes3.credit.sys.service.SysUserRoleService;
import com.taoyes3.credit.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @Resource
    private SysUserRoleService sysUserRoleService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private TokenManager tokenManager;

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
        // 密码加密 
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
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
        // 密码加密
        String password = StrUtil.isBlank(sysUser.getPassword()) ? null : sysUser.getPassword();
        sysUser.setPassword(passwordEncoder.encode(password));

        if (Objects.equals(1L, sysUser.getId()) && sysUser.getStatus() == 0) {
            throw new CreditBindException("admin用户不可以被禁用");
        }
        sysUserService.updateUserAndUserRole(sysUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody List<Long> ids) {
        // Constant.SUPER_ADMIN_ID 类型需要为Long,
        // contains会比较Constant.SUPER_ADMIN_ID和ids内部（即子元素）的数据类型
        if (ids.contains(Constant.SUPER_ADMIN_ID)) {
            throw new CreditBindException("系统管理员不能删除");
        }
        // 前用户不能删除 
        if (ids.contains(Long.valueOf(AuthUserContext.get().getUserId()))) {
            throw new CreditBindException("当前用户不能删除");
        }
        sysUserService.deleteBatch(ids);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<SysUser> info(@PathVariable Long id) {
        SysUser sysUser = sysUserService.getById(id);
        // 查询用户对应的角色
        List<SysUserRole> sysUserRoles = sysUserRoleService.list(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, id));
        List<Long> roleIdList = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        sysUser.setRoleIdList(roleIdList);
        return ResponseEntity.ok(sysUser);
    }

    /**
     * 获取登录的用户信息
     *
     * @return
     */
    @GetMapping("/info")
    public ResponseEntity<SysUser> info() {
        return ResponseEntity.ok(sysUserService.getById(AuthUserContext.get().getUserId()));
    }

    @PutMapping("/password")
    public ResponseEntity<Object> password(@RequestBody UpdatePasswordDto passwordDto) {
        Long userId = Long.valueOf(AuthUserContext.get().getUserId());
        SysUser sysUser = sysUserService.getById(userId);
        if (!passwordEncoder.matches(passwordDto.getPassword(), sysUser.getPassword())) {
            throw new CreditBindException("原密码不正确");
        }
        String newPassword = passwordEncoder.encode(passwordDto.getNewPassword());
        // 更新密码
        sysUserService.update(new LambdaUpdateWrapper<SysUser>().eq(SysUser::getId, userId).set(SysUser::getPassword, newPassword));
        // 清除所有 token
        tokenManager.deleteAllToken(String.valueOf(SysTypeEnum.ADMIN.getValue()), String.valueOf(userId));
        return ResponseEntity.ok().build();
    }
}
