package com.poemSys.common.security;

import cn.hutool.core.util.StrUtil;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.service.SysUserService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *jwt身份认证过滤器
 */
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter
{
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    GetLoginSysUserService getLoginSysUserByIdService;

    @Autowired
    SysUserService sysUserService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager)
    {
        super(authenticationManager);
    }

    //重写过滤流程
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        String jwt = request.getHeader(jwtUtils.getHeader());
        //如果没有jwt就在过滤器链中继续向后走
        if(StrUtil.isBlankOrUndefined(jwt))
        {
            chain.doFilter(request, response);
            return;
        }

        Claims claim = jwtUtils.getClaimByToken(jwt);
        if(claim == null)
        {
            log.info("token 异常");
            chain.doFilter(request, response);
            return;
        }
        if(jwtUtils.isTokenExpired(claim))
        {
            log.info("token 已过期");
            chain.doFilter(request, response);
            return;
        }
        //通过jwt主体中拿到用户Id（创建jwt时设置的）
        Long userId = Long.parseLong(claim.getSubject());
        //判断是否已经被封号了
        SysUser sysUser = sysUserService.getSysUserById(userId);
        if(sysUser!=null)
        {
            LocalDateTime unlockTime = sysUser.getUnlockTime();
            if (unlockTime != null && unlockTime.isAfter(LocalDateTime.now()))
            {
                chain.doFilter(request, response);
                return;
            }
        }

        //获取用户权限等信息


        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(userId,
                null, userDetailService.getUserAuthority(userId));
        //参数传递authentication对象，来建立安全上下文（security context）
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(request, response);
    }
}
