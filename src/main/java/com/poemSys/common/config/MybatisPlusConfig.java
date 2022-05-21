package com.poemSys.common.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置mybatis-plus
 */
@Configuration
@MapperScan("com.poemSys.common.mapper")
public class MybatisPlusConfig
{
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor()
    {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //分页插件
//        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        //防全表更新插件
//        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer()
    {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }

    @Bean
    public MybatisPlusInterceptor paginationInterceptor()
    {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();

        //设置请求的页面大于最大页后操作，true调回到首页，false继续请求，默认false
        //paginationInterceptor.setOverflow(false);
        //设置最大单页限制数量，默认500条，-1不受限制
        //paginationInterceptor.setLimit(500);
        //开启count的join优化，只针对部分Left join

        //分页拦截器
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setOverflow(true);
        paginationInnerInterceptor.setMaxLimit(500L);
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
        return mybatisPlusInterceptor;
    }
}
