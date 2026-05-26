package com.example.demo.config;

import cn.dev33.satoken.fun.strategy.SaCorsHandleFunction;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 全局跨域配置（标准 Spring Boot 写法，无任何报错）
 */
@Configuration
public class CosConfig {

    /**
     * CORS 跨域处理策略
     */
    @Bean
    public SaCorsHandleFunction corsHandle() {
        return (req, res, sto) -> {
            // 允许指定域访问跨域资源
            res.setHeader("Access-Control-Allow-Origin", "*")
                    // 允许所有请求方式
                    .setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE")
                    // 预检请求有效时间
                    .setHeader("Access-Control-Max-Age", "3600")
                    // 允许的 header 参数
                    .setHeader("Access-Control-Allow-Headers", "*");

            // 如果是预检请求，则立即返回到前端
            SaRouter.match(SaHttpMethod.OPTIONS)
                    .free(r -> System.out.println("-------OPTIONS预检请求，不做处理"))
                    .back();
        };
    }
}