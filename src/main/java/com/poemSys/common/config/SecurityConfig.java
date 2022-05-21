package com.poemSys.common.config;

import com.poemSys.common.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * spring security配置类
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Autowired
    LoginSuccessHandler loginSuccessHandler;


    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    JwtAccessDeniedHandler jwtAccessDeniedHandler;


    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    JwtLogoutSuccessHandler jwtLogoutSuccessHandler;

    //jwt身份认证过滤器
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception
    {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }

    //密码加密
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    //将重写的登录验证过滤器放入到容器中
    @Bean
    LoginFilter loginFilter() throws Exception
    {
        LoginFilter loginFilter = new LoginFilter();
        //登录成功/失败的处理器/过滤的url
        loginFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        loginFilter.setAuthenticationFailureHandler(loginFailureHandler);
        loginFilter.setFilterProcessesUrl("/api/login");
        //重用WebSecurityConfigurerAdapter配置的AuthenticationManager，不然要自己组装AuthenticationManager
        //必须要设置，不要会启动报错
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        return loginFilter;
    }

    //拦截白名单
    private static final String[] URL_WHITELIST = {
            "/ws/**",
            "/test/**",
            "/api/login",
            "/api/logout",
            "/api/captcha",
            "/api/user/sendEmailToAddress",
            "/api/user/sendEmailToLoginUser",
            "/api/user/register",
            "/api/user/findPassword",
            "/api/poetry/**"
    };

    protected void configure(HttpSecurity http) throws Exception
    {
        //声明跨域问题
        http.cors().and().csrf().disable()

                //重写验证用户名、密码和验证码，接受json格式数据
                .addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class)
                // 登录配置
                .formLogin()
                .loginPage("/login")//登录页面url

                //退出登录
                .and()
                .logout()
                .logoutUrl("/api/logout")
                .logoutSuccessUrl("/login")
                .logoutSuccessHandler(jwtLogoutSuccessHandler)

                // 禁用session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 配置拦截规则
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()//跳过白名单
                .anyRequest().authenticated()

                // 认证异常和权限不足处理器
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)//认证失败处理器
                .accessDeniedHandler(jwtAccessDeniedHandler)//权限不足处理器

                // 配置自定义的过滤器
                .and()
                .addFilter(jwtAuthenticationFilter())
        ;

    }

    //重写自定义的userDetailService
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailService);
    }
}