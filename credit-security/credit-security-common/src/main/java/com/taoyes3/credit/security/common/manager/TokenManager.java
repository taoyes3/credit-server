package com.taoyes3.credit.security.common.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.taoyes3.credit.security.common.bo.TokenInfoBO;
import com.taoyes3.credit.security.common.bo.UserInfoInTokenBO;
import com.taoyes3.credit.security.common.enums.SysTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * token管理 1.登陆返回token 2.刷新token 3.清除用户过去token 4.校验token
 * 
 * @author taoyes3
 * @date 2022/9/24 9:44
 */
@Component
@Slf4j
public class TokenManager {
    /**
     * oauth 授权相关key
     */
    String OAUTH_PREFIX = "credit_oauth:";

    /**
     * token 授权相关key
     */
    String OAUTH_TOKEN_PREFIX = OAUTH_PREFIX + "token:";

    /**
     * 保存token 缓存使用key
     */
    String ACCESS = OAUTH_TOKEN_PREFIX + "access:";

    /**
     * 刷新token 缓存使用key
     */
    String REFRESH_TO_ACCESS = OAUTH_TOKEN_PREFIX + "refresh_to_access:";

    /**
     * 根据uid获取保存的token key缓存使用的key
     */
    String UID_TO_ACCESS = OAUTH_TOKEN_PREFIX + "uid_to_access:";
    
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    public void storeAccessToken(UserInfoInTokenBO userInfoInTokenBO) {
        String accessToken = IdUtil.simpleUUID();
        String refreshToken = IdUtil.simpleUUID();
        // TokenInfoBO 信息
        TokenInfoBO tokenInfoBO = new TokenInfoBO();
        tokenInfoBO.setUserInfoInTokenBO(userInfoInTokenBO);
        tokenInfoBO.setAccessToken(accessToken);
        tokenInfoBO.setRefreshToken(refreshToken);
        tokenInfoBO.setExpiresIn(getExpiresIn(userInfoInTokenBO.getSysType()));
        
        String uidToAccessKeyStr = getUserIdToAccessKey(getApprovalKey(userInfoInTokenBO));
        String accessKeyStr = getAccessKey(accessToken);
        String refreshToAccessKeyStr = getRefreshToAccessKey(refreshToken);

        // 一个用户会登陆很多次，每次登录的token都会存在 UID_TO_ACCESS 里面
        // 但是每次保存都会更新这个key的时间，而key里面的token有可能会过期，过期就要移除掉
        ArrayList<Object> existsAccessTokenBytes = new ArrayList<>();
        // 新的token数据
        existsAccessTokenBytes.add((accessToken + StrUtil.COLON + refreshToken).getBytes(StandardCharsets.UTF_8));

        Long size = redisTemplate.opsForSet().size(uidToAccessKeyStr);
        if (size != null && size != 0) {
            List<String> tokenInfoBoList = stringRedisTemplate.opsForSet().pop(uidToAccessKeyStr, size);
            if (tokenInfoBoList != null) {
                // for (String tokenInfoBO : tokenInfoBoList) {
                //    
                // }
            }
        }
    }

    private String getRefreshToAccessKey(String refreshToken) {
        return REFRESH_TO_ACCESS + refreshToken;
    }

    private String getAccessKey(String accessToken) {
        return ACCESS + accessToken;
    }

    private String getApprovalKey(UserInfoInTokenBO userInfoInTokenBO) {
        return getApprovalKey(userInfoInTokenBO.getSysType().toString(), userInfoInTokenBO.getUserId());
    }
    
    private String getApprovalKey(String sysType, String userId) {
        return userId == null ? sysType : sysType + StrUtil.COLON + userId;
    }

    private String getUserIdToAccessKey(String approvalKey) {
        return UID_TO_ACCESS + approvalKey;
    }

    private Integer getExpiresIn(Integer sysType) {
        // 1800秒
        int expiresIn = 1800;
        // 普通用户token过期时间 1小时
        if (Objects.equals(sysType, SysTypeEnum.ORDINARY.getValue())) {
            expiresIn = expiresIn * 2;
        }
        // 系统管理员的token过期时间 2小时
        if (Objects.equals(sysType, SysTypeEnum.ADMIN.getValue())) {
            expiresIn = expiresIn * 4;
        }
        
        return expiresIn;
    }
}
