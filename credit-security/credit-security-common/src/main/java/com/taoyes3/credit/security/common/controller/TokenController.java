package com.taoyes3.credit.security.common.controller;

import com.taoyes3.credit.security.common.bo.TokenInfoBO;
import com.taoyes3.credit.security.common.dto.RefreshTokenDTO;
import com.taoyes3.credit.security.common.manager.TokenManager;
import com.taoyes3.credit.security.common.vo.TokenInfoVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author taoyes3
 * @date 2022/9/30 11:56
 */
@RestController
@RequestMapping("/token")
public class TokenController {
    @Resource
    private TokenManager tokenManager;
    @Resource
    private MapperFacade mapperFacade;
    
    @PostMapping("/refresh")
    public ResponseEntity<TokenInfoVO> refresh(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        TokenInfoBO tokenInfoBO = tokenManager.refreshToken(refreshTokenDTO.getRefreshToken());
        return ResponseEntity.ok(mapperFacade.map(tokenInfoBO, TokenInfoVO.class));
    }
}
