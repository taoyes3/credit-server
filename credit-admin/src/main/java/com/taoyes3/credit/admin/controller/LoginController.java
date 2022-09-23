package com.taoyes3.credit.admin.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taoyes3.credit.common.exception.CreditBindException;
import com.taoyes3.credit.common.util.RedisUtil;
import com.taoyes3.credit.security.admin.dto.CaptchaAuthenticationDTO;
import com.taoyes3.credit.security.common.enums.SysTypeEnum;
import com.taoyes3.credit.security.common.manager.PasswordCheckManager;
import com.taoyes3.credit.security.common.manager.PasswordManager;
import com.taoyes3.credit.sys.model.SysUser;
import com.taoyes3.credit.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

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
    
    @PostMapping
    public void store(@Valid @RequestBody CaptchaAuthenticationDTO captchaAuthenticationDTO) {
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
        
        log.info("test login...");
    }
    
    @GetMapping("/test")
    public void test() {
        redisUtil.set("1checkPrefix + usernameOrMobile1", 111, 1800);
    }
}
