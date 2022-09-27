package com.taoyes3.credit.security.common.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.taoyes3.credit.security.common.bo.UserInfoInTokenBO;

/**
 * @author taoyes3
 * @date 2022/9/27 9:52
 */
public class AuthUserContext {
    private static final ThreadLocal<UserInfoInTokenBO> USER_INFO_IN_TOKEN_BO_THREAD_LOCAL = new TransmittableThreadLocal<>();
    
    public static UserInfoInTokenBO get() {
        return USER_INFO_IN_TOKEN_BO_THREAD_LOCAL.get();
    }
    
    public static void set(UserInfoInTokenBO userInfoInTokenBO) {
        USER_INFO_IN_TOKEN_BO_THREAD_LOCAL.set(userInfoInTokenBO);
    }
}
