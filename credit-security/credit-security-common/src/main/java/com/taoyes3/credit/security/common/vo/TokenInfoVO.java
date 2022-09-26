package com.taoyes3.credit.security.common.vo;

import lombok.Data;

/**
 * @author taoyes3
 * @date 2022/9/26 15:30
 */
@Data
public class TokenInfoVO {
    private String accessToken;
    
    private String refreshToken;
    
    private Integer expiresIn;
}
