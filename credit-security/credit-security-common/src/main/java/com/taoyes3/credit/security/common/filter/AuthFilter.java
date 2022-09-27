package com.taoyes3.credit.security.common.filter;

import com.taoyes3.credit.security.common.adapter.AuthConfigAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author taoyes3
 * @date 2022/9/27 10:14
 */
@Component
@Slf4j
public class AuthFilter implements Filter {
    @Resource
    private AuthConfigAdapter authConfigAdapter;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        // 获取本次请求的URI
        String requestURI = req.getRequestURI();
        // 非授权路径
        List<String> excludePathPatterns = authConfigAdapter.excludePathPatterns();
        // 路径匹配器，支持通配符
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        // 匹配非授权路径，放行
        for (String excludePathPattern : excludePathPatterns) {
            if (antPathMatcher.match(excludePathPattern, requestURI)) {
                chain.doFilter(req, resp);
                return;
            }
        }

        String accessToken = req.getHeader("Authorization");
    }
}
