package com.taoyes3.credit.security.common.bo;

import lombok.Data;

/**
 * @author taoyes3
 * @date 2022/9/24 11:06
 */
@Data
public class TokenInfoBO {
    /**
     * 保存在token信息里面的用户信息
     */
    private UserInfoInTokenBO userInfoInTokenBO;

    /**
     * token信息
     */
    private String accessToken;

    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * 在多少秒后过期
     */
    private Integer expiresIn;
}
