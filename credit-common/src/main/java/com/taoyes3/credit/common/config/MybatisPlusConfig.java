package com.taoyes3.credit.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author taoyes3
 * @date 2022/9/14 16:48
 */
@Configuration
@MapperScan({"com.taoyes3.credit.**.dao"})
public class MybatisPlusConfig {
}
