package com.taoyes3.credit.security.admin.util;

import com.taoyes3.credit.security.common.bo.UserInfoInTokenBO;
import com.taoyes3.credit.security.common.util.AuthUserContext;
import lombok.experimental.UtilityClass;

/**
 * @author taoyes3
 * @date 2022/9/29 13:39
 */
@UtilityClass
public class SecurityUtils {
    public void getSysUser() {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
    }
}
