package com.taoyes3.credit.security.admin.dto;

import com.taoyes3.credit.security.common.dto.AuthenticationDTO;
import lombok.Data;

/**
 * @author taoyes3
 * @date 2022/9/22 15:19
 */
@Data
public class CaptchaAuthenticationDTO extends AuthenticationDTO {
    private String captchaVerification;
}
