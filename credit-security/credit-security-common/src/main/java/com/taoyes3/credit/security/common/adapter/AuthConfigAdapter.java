package com.taoyes3.credit.security.common.adapter;

import java.util.List;

/**
 * @author taoyes3
 * @date 2022/9/27 10:22
 */
public interface AuthConfigAdapter {

    /**
     * 授权登录
     * @return 需要授权登录的路径列表
     */
    List<String> pathPatterns();

    /**
     * 非授权登录
     * @return 不需要授权登录的路径列表
     */
    List<String> excludePathPatterns();
}
