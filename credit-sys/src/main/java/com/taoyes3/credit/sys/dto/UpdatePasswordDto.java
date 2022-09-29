package com.taoyes3.credit.sys.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author taoyes3
 * @date 2022/9/29 14:16
 */
@Data
public class UpdatePasswordDto {
    @NotBlank(message = "旧密码不能为空")
    @Size(max = 50)
    private String password;

    @NotBlank(message = "新密码不能为空")
    @Size(max = 50)
    private String newPassword;
}
