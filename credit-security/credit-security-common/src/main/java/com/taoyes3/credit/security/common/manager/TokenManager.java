package com.taoyes3.credit.security.common.manager;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.AES;
import com.taoyes3.credit.common.exception.CreditBindException;
import com.taoyes3.credit.common.util.PrincipalUtil;
import com.taoyes3.credit.security.common.bo.TokenInfoBO;
import com.taoyes3.credit.security.common.bo.UserInfoInTokenBO;
import com.taoyes3.credit.security.common.enums.SysTypeEnum;
import com.taoyes3.credit.security.common.vo.TokenInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.HttpStatus;
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

    /**
     * 用于aes签名的key，16位
     */
    @Value("${auth.token.signKey:-credit--token--}")
    private String tokenSignKey;
    
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 将用户的部分信息存储在token中，并返回token信息
     * @param userInfoInTokenBO 用户在token中的信息
     * @return token信息
     */
    public TokenInfoBO storeAccessToken(UserInfoInTokenBO userInfoInTokenBO) {
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

        // 一个用户会登录很多次，每次登录的token都会存在 UID_TO_ACCESS 里面
        // 但是每次保存都会更新这个key的时间，而key里面的token有可能会过期，过期就要移除掉
        ArrayList<byte[]> existsAccessTokenBytes = new ArrayList<>();
        // 新的token数据
        existsAccessTokenBytes.add((accessToken + StrUtil.COLON + refreshToken).getBytes(StandardCharsets.UTF_8));

        // redisTemplate 或 stringRedisTemplate 都可以使用
        Long size = stringRedisTemplate.opsForSet().size(uidToAccessKeyStr);
        if (size != null && size != 0) {
            List<String> tokenInfoBoList = stringRedisTemplate.opsForSet().pop(uidToAccessKeyStr, size);
            if (tokenInfoBoList != null) {
                for (String accessTokenWithRefreshToken : tokenInfoBoList) {
                    // 字符串转为数组
                    String[] accessTokenWithRefreshTokenArr = accessTokenWithRefreshToken.split(StrUtil.COLON);
                    String accessTokenData = accessTokenWithRefreshTokenArr[0];
                    // 如果redis中没有对应的accessToken的key，则过滤掉
                    if (BooleanUtil.isTrue(stringRedisTemplate.hasKey(getAccessKey(accessTokenData)))) {
                        existsAccessTokenBytes.add(accessTokenWithRefreshToken.getBytes(StandardCharsets.UTF_8));
                    }
                }
            }
        }
        // pipeline批量处理
        redisTemplate.executePipelined((RedisCallback<Object>) redisConnection -> {
            long expiresIn = tokenInfoBO.getExpiresIn();
            byte[] uidKey = uidToAccessKeyStr.getBytes(StandardCharsets.UTF_8);
            byte[] refreshKey = refreshToAccessKeyStr.getBytes(StandardCharsets.UTF_8);
            byte[] accessKey = accessKeyStr.getBytes(StandardCharsets.UTF_8);
            // 没有序列化存储，所以使用redisTemplate读取数据时会报错（读取时会进行序列化转换）
            // 因为字符串自己几乎就已经是byte array了，不需要自己处理，所以可以使用stringRedisTemplate读取数据
            redisConnection.sAdd(uidKey, ArrayUtil.toArray(existsAccessTokenBytes, byte[].class));
            // 通过uid + sysType 保存access_token，当需要禁用用户的时候，可以根据uid + sysType 禁用用户
            redisConnection.expire(uidKey, expiresIn);
            // 通过refresh_token获取用户的access_token从而刷新token
            redisConnection.setEx(refreshKey, expiresIn, accessToken.getBytes(StandardCharsets.UTF_8));
            // 序列化 userInfoInTokenBO 对象
            RedisSerializer<Object> redisSerializer = new GenericJackson2JsonRedisSerializer();
            // 通过access_token保存用户id，uid
            redisConnection.setEx(accessKey, expiresIn, Objects.requireNonNull(redisSerializer.serialize(userInfoInTokenBO)));
            return null;
        });
        // 返回给前端是加密的token
        tokenInfoBO.setAccessToken(encryptToken(accessToken, userInfoInTokenBO.getSysType()));
        tokenInfoBO.setRefreshToken(encryptToken(refreshToken, userInfoInTokenBO.getSysType()));
        
        return tokenInfoBO;
    }

    /**
     * 根据accessToken 获取用户信息
     * @param accessToken accessToken
     * @param needDecrypt 是否需要解密
     * @return 用户信息
     */
    public UserInfoInTokenBO getUserInfoByAccessToken(String accessToken, boolean needDecrypt) {
        if (StrUtil.isBlank(accessToken)) {
            throw new CreditBindException(HttpStatus.UNAUTHORIZED.value(), "accessToken is blank");
        }
        String realAccessToken = needDecrypt ? decryptToken(accessToken) : accessToken;
        UserInfoInTokenBO userInfoInTokenBO = (UserInfoInTokenBO) redisTemplate.opsForValue().get(getAccessKey(realAccessToken));
        if (userInfoInTokenBO == null) {
            throw new CreditBindException(HttpStatus.UNAUTHORIZED.value(), "accessToken 已失效");
        }
        return userInfoInTokenBO;
    }

    /**
     * 对 token 进行解密
     * @param accessToken accessToken
     * @return 解密后的 token
     */
    private String decryptToken(String accessToken) {
        AES aes = new AES(tokenSignKey.getBytes(StandardCharsets.UTF_8));
        String decryptStr = aes.decryptStr(accessToken);
        // 获取解密后的组成部分
        String decryptToken = decryptStr.substring(0, 32);
        // 防止解密后的token是脚本，从而对redis进行攻击，uuid只能是数字和小写字母
        if (!PrincipalUtil.isSimpleChar(decryptToken)) {
            throw new CreditBindException(HttpStatus.UNAUTHORIZED.value(), "token error");
        }
        // 创建token的时间，token使用时效性，防止攻击者通过一堆的尝试找到aes的密码，虽然aes是目前几乎最好的加密算法
        long createTokenTime = Long.parseLong(decryptStr.substring(32,45));
        // 系统类型
        int sysType = Integer.parseInt(decryptStr.substring(45));
        // token的过期时间
        Integer expiresIn = getExpiresIn(sysType);
        long seconds = 1000L;
        if (System.currentTimeMillis() - createTokenTime > expiresIn * seconds) {
            throw new CreditBindException(HttpStatus.UNAUTHORIZED.value(), "token error");
        }
        
        return decryptToken;
    }

    /**
     * 对 token 进行加密
     * @param accessToken accessToken
     * @param sysType 系统类型
     * @return 加密后的 token
     */
    private String encryptToken(String accessToken, Integer sysType) {
        AES aes = new AES(tokenSignKey.getBytes(StandardCharsets.UTF_8));
        return aes.encryptBase64(accessToken + System.currentTimeMillis() + sysType);
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

    public TokenInfoVO storeAndGetVo(UserInfoInTokenBO userInfoInTokenBO) {
        TokenInfoBO tokenInfoBO = storeAccessToken(userInfoInTokenBO);

        TokenInfoVO tokenInfoVO = new TokenInfoVO();
        tokenInfoVO.setAccessToken(tokenInfoBO.getAccessToken());
        tokenInfoVO.setRefreshToken(tokenInfoBO.getRefreshToken());
        tokenInfoVO.setExpiresIn(tokenInfoBO.getExpiresIn());
        
        return tokenInfoVO;
    }
}
