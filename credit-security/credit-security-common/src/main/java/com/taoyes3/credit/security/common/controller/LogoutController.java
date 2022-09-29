package com.taoyes3.credit.security.common.controller;

import cn.hutool.core.util.StrUtil;
import com.taoyes3.credit.security.common.manager.TokenManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author taoyes3
 * @date 2022/9/29 16:22
 */
@RestController
public class LogoutController {
    @Resource
    private TokenManager tokenManager;
    
    @DeleteMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (StrUtil.isNotBlank(accessToken)) {
            // 删除该用户在该系统当前的token
            tokenManager.deleteCurrentToken(accessToken);
        }
        return ResponseEntity.ok().build();
    }
}
