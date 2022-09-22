package com.taoyes3.credit.admin.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.taoyes3.credit.common.exception.CreditBindException;
import com.taoyes3.credit.security.admin.dto.CaptchaAuthenticationDTO;
import com.taoyes3.credit.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
    @PostMapping
    public void store(@Valid @RequestBody CaptchaAuthenticationDTO captchaAuthenticationDTO) {
        // 登陆后台登录需要再校验一遍验证码
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(captchaAuthenticationDTO.getCaptchaVerification());
        ResponseModel responseModel = captchaService.verification(captchaVO);
        if (!responseModel.isSuccess()) {
            throw new CreditBindException("验证码有误或已过期");
        }
    }
}
