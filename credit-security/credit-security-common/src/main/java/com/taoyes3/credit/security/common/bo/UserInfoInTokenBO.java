package com.taoyes3.credit.security.common.bo;

import lombok.Data;

import java.util.Set;

/**
 * @author taoyes3
 * @date 2022/9/23 17:43
 */
@Data
public class UserInfoInTokenBO {
    /**
     * 用户在自己系统的用户id
     */
    private String userId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 系统类型
     * @see com.taoyes3.credit.security.common.enums.SysTypeEnum
     */
    private Integer sysType;

    /**
     * 是否是管理员
     */
    private Integer isAdmin;

    /**
     * 权限列表
     */
    private Set<String> perms;

    /**
     * 状态 1 正常 0 无效
     */
    private Boolean enabled;
    
}
