package com.taoyes3.credit.security.common.enums;

/**
 * @author taoyes3
 * @date 2022/9/23 10:25
 */
public enum SysTypeEnum {
    /**
     * 普通用户
     */
    ORDINARY(0),

    /**
     * 后台
     */
    ADMIN(1);
    
    private final Integer value;

    SysTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
