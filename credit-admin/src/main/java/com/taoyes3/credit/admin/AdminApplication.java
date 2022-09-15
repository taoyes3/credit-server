package com.taoyes3.credit.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author taoyes3
 * @date 2022/9/14 17:25
 */
@SpringBootApplication
//指定了其他模块包路径下的标记类可以被扫描（不加的话，默认当前模块）
@ComponentScan("com.taoyes3.credit")
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
