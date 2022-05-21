package com.poemSys.common.security;

import cn.hutool.json.JSONUtil;
import com.poemSys.common.bean.Result;
import com.poemSys.common.service.SysUserService;
import com.poemSys.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 登录成功处理器
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler
{

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    SysUserService sysUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException
    {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();

        String username = authentication.getName();
        String userId = Long.toString(sysUserService.getSysUserByUsername(username).getId());

        // 生成jwt，并放置到请求头中
        String jwt = jwtUtils.generateToken(userId);
        response.setHeader(jwtUtils.getHeader(), jwt);

        Result result = new Result(0, "登录成功", null);

        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));

        outputStream.flush();
        outputStream.close();
    }

}