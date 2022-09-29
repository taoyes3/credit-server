package com.taoyes3.credit.security.common.manager;

import cn.hutool.core.util.StrUtil;
import com.taoyes3.credit.common.exception.CreditBindException;
import com.taoyes3.credit.common.util.IPHelper;
import com.taoyes3.credit.common.util.RedisUtil;
import com.taoyes3.credit.security.common.enums.SysTypeEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author taoyes3
 * @date 2022/9/23 10:15
 */
@Component
public class PasswordCheckManager {
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 半小时内最多错误10次
     */
    private static final int TIMES_CHECK_INPUT_PASSWORD_NUM = 10;

    private static final String CHECK_VALID_CODE_NUM_PREFIX = "checkUserInputErrorPassword_";

    public void checkPassword(SysTypeEnum sysTypeEnum, String usernameOrMobile, String rawPassword, String encodedPassword) {
        String checkPrefix = sysTypeEnum.getValue() + CHECK_VALID_CODE_NUM_PREFIX + IPHelper.getIpAddr();
        int count = 0;
        if (redisUtil.hasKey(checkPrefix + usernameOrMobile)) {
            count = redisUtil.get(checkPrefix + usernameOrMobile);
        }
        if (count > TIMES_CHECK_INPUT_PASSWORD_NUM) {
            throw new CreditBindException("半小时内密码输入错误十次，已限制登录30分钟");
        }
        // 半小时后失效
        redisUtil.set(checkPrefix + usernameOrMobile, count, 1800);
        // 密码不正确
        if (StrUtil.isBlank(encodedPassword) || !passwordEncoder.matches(rawPassword, encodedPassword)) {
            count++;
            // 半小时后失效
            redisUtil.set(checkPrefix + usernameOrMobile, count, 1800);
            throw new CreditBindException("账号或密码不正确");
        }
    }
}
