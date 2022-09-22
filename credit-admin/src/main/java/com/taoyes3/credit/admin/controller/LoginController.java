package com.taoyes3.credit.admin.controller;

import com.taoyes3.credit.security.admin.dto.CaptchaAuthenticationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author taoyes3
 * @date 2022/9/22 9:48
 */
@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @PostMapping
    public void store(@Valid @RequestBody CaptchaAuthenticationDTO captchaAuthenticationDTO) {
        
    }
}
