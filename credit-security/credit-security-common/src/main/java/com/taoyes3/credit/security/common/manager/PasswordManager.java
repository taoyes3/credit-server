package com.taoyes3.credit.security.common.manager;

import cn.hutool.crypto.symmetric.AES;
import com.taoyes3.credit.common.exception.CreditBindException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author taoyes3
 * @date 2022/9/23 9:01
 */
@Component
@Slf4j
public class PasswordManager {
    @Value("${auth.password.signKey:-credit-password}")
    private String passwordSignKey;
    
    public String decryptPassword(String data) {
        AES aes = new AES(passwordSignKey.getBytes(StandardCharsets.UTF_8));
        try {
            String decryptStr = aes.decryptStr(data);
            return decryptStr.substring(13);
        } catch (Exception e) {
            throw new CreditBindException("AES解密错误", e);
        }
    }
}
