package com.taoyes3.credit.security.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author taoyes3
 * @date 2022/9/30 11:57
 */
@Data
public class RefreshTokenDTO {
    @NotBlank(message = "refreshToken不能为空")
    private String refreshToken;
}
