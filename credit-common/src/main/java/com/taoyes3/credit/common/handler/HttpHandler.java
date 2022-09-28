package com.taoyes3.credit.common.handler;

import cn.hutool.core.util.CharsetUtil;
import com.taoyes3.credit.common.exception.CreditBindException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author taoyes3
 * @date 2022/9/28 10:51
 */
@Component
@Slf4j
public class HttpHandler {
    public void printServerResponseToWeb(String str, int status) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            log.error("requestAttributes is null, can not print to web");
            return;
        }
        HttpServletResponse response = requestAttributes.getResponse();
        if (response == null) {
            log.error("httpServletResponse is null, can not print to web");
            return;
        }
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status);

        try {
            PrintWriter printWriter = response.getWriter();
            printWriter.write(str);
        } catch (IOException e) {
            throw new CreditBindException("io 异常", e);
        }
    }
}
