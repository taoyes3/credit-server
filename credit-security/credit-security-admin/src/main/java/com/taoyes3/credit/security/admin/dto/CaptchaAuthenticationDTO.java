package com.taoyes3.credit.security.admin.dto;

import com.taoyes3.credit.security.common.dto.AuthenticationDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author taoyes3
 * @date 2022/9/22 15:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CaptchaAuthenticationDTO extends AuthenticationDTO {
    private String captchaVerification;
}
