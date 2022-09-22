package com.taoyes3.credit.security.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用于登陆传递账号密码
 * 
 * @author taoyes3
 * @date 2022/9/22 15:06
 */
@Data
public class AuthenticationDTO {
    @NotBlank(message = "username 不能为空")
    private String username;
    
    @NotBlank(message = "password 不能为空")
    private String password;
}
