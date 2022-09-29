package com.taoyes3.credit.security.common.config;

import cn.hutool.core.util.ArrayUtil;
import com.taoyes3.credit.security.common.adapter.AuthConfigAdapter;
import com.taoyes3.credit.security.common.adapter.DefaultAuthConfigAdapter;
import com.taoyes3.credit.security.common.filter.AuthFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import javax.annotation.Resource;
import javax.servlet.DispatcherType;

/**
 * @author taoyes3
 * @date 2022/9/28 15:27
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class AuthConfig {
    @Resource
    private AuthFilter authFilter;
    
    @Bean
    @ConditionalOnMissingBean
    public AuthConfigAdapter authConfigAdapter() {
        return new DefaultAuthConfigAdapter();
    }
    
    @Bean
    @Lazy
    public FilterRegistrationBean<AuthFilter> filterFilterRegistration(AuthConfigAdapter authConfigAdapter) {
        FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>();
        // 添加过滤器
        registration.setFilter(authFilter);
        // 设置过滤路径，/*所有路径
        registration.addUrlPatterns(ArrayUtil.toArray(authConfigAdapter.pathPatterns(), String.class));
        registration.setName("authFilter");
        // 设置优先级
        registration.setOrder(0);
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        return registration;
    }
}
