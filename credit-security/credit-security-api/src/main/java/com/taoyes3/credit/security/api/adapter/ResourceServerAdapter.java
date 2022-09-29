package com.taoyes3.credit.security.api.adapter;

import com.taoyes3.credit.security.common.adapter.DefaultAuthConfigAdapter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author taoyes3
 * @date 2022/9/29 10:37
 */
@Component
public class ResourceServerAdapter extends DefaultAuthConfigAdapter {
    public static final List<String> EXCLUDE_PATH = Arrays.asList(
            "/test",
            "/captcha111/**"
    );

    @Override
    public List<String> excludePathPatterns() {
        return EXCLUDE_PATH;
    }
}
