package com.taoyes3.credit.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.anji.captcha.service.CaptchaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taoyes3.credit.common.exception.CreditBindException;
import com.taoyes3.credit.common.util.RedisUtil;
import com.taoyes3.credit.security.admin.dto.CaptchaAuthenticationDTO;
import com.taoyes3.credit.security.common.bo.UserInfoInTokenBO;
import com.taoyes3.credit.security.common.enums.SysTypeEnum;
import com.taoyes3.credit.security.common.manager.PasswordCheckManager;
import com.taoyes3.credit.security.common.manager.PasswordManager;
import com.taoyes3.credit.security.common.manager.TokenManager;
import com.taoyes3.credit.security.common.vo.TokenInfoVO;
import com.taoyes3.credit.sys.constant.Constant;
import com.taoyes3.credit.sys.model.SysMenu;
import com.taoyes3.credit.sys.model.SysUser;
import com.taoyes3.credit.sys.service.SysMenuService;
import com.taoyes3.credit.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author taoyes3
 * @date 2022/9/22 9:48
 */
@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @Resource
    private CaptchaService captchaService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private PasswordManager passwordManager;
    @Resource
    private PasswordCheckManager passwordCheckManager;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private SysMenuService sysMenuService;
    @Resource
    private TokenManager tokenManager;
    
    @PostMapping
    public ResponseEntity<TokenInfoVO> store(@Valid @RequestBody CaptchaAuthenticationDTO captchaAuthenticationDTO) {
        // 登陆后台登录需要再校验一遍验证码
        // CaptchaVO captchaVO = new CaptchaVO();
        // captchaVO.setCaptchaVerification(captchaAuthenticationDTO.getCaptchaVerification());
        // ResponseModel responseModel = captchaService.verification(captchaVO);
        // if (!responseModel.isSuccess()) {
        //     throw new CreditBindException("验证码有误或已过期");
        // }
        SysUser sysUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, captchaAuthenticationDTO.getUsername()));
        if (sysUser == null) {
            throw new CreditBindException("账号或密码不正确");
        }
        //半小时内密码输入错误十次，限制登录30分钟
        // String decryptPassword = passwordManager.decryptPassword(captchaAuthenticationDTO.getPassword());
        String decryptPassword = captchaAuthenticationDTO.getPassword();
        passwordCheckManager.checkPassword(SysTypeEnum.ADMIN, captchaAuthenticationDTO.getUsername(), decryptPassword, sysUser.getPassword());

        UserInfoInTokenBO userInfoInTokenBO = new UserInfoInTokenBO();
        userInfoInTokenBO.setUserId(String.valueOf(sysUser.getId()));
        userInfoInTokenBO.setNickName(sysUser.getUsername());
        userInfoInTokenBO.setSysType(SysTypeEnum.ADMIN.getValue());
        userInfoInTokenBO.setEnabled(sysUser.getStatus() == 1);
        userInfoInTokenBO.setPerms(getUserPermissions(sysUser.getId()));
        // 存储token返回vo
        TokenInfoVO tokenInfoVO = tokenManager.storeAndGetVo(userInfoInTokenBO);
        log.info("test login...");
        return ResponseEntity.ok(tokenInfoVO);
    }
    
    private Set<String> getUserPermissions(Long userId) {
        List<String> permissionList;
        // 系统管理员，拥有最高权限
        if (userId.equals(Constant.SUPER_ADMIN_ID)) {
            List<SysMenu> sysMenuList = sysMenuService.list();
            permissionList = sysMenuList.stream().map(SysMenu::getPerms).collect(Collectors.toList());
        } else {
            permissionList = sysMenuService.queryAllPerms(userId);
        }
        
        return permissionList.stream().flatMap((permission)->{
            if (StrUtil.isBlank(permission)) {
                return null;
            }
            return Arrays.stream(permission.trim().split(StrUtil.COMMA));
        }).collect(Collectors.toSet());
    }
}
