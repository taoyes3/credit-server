package com.taoyes3.credit.security.admin.adapter;

import com.taoyes3.credit.security.common.adapter.DefaultAuthConfigAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author taoyes3
 * @date 2022/9/29 10:16
 */
@Component
@Slf4j
public class ResourceServerAdapter extends DefaultAuthConfigAdapter {
    public static final List<String> EXCLUDE_PATH = Arrays.asList(
            "/login",
            "/captcha/**"
    );

    @Override
    public List<String> excludePathPatterns() {
        log.info("EXCLUDE_PATH的值：11111111");
        return EXCLUDE_PATH;
    }
}
