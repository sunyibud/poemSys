package com.poemSys.common.security;

import cn.hutool.json.JSONUtil;
import com.poemSys.common.bean.Const;
import com.poemSys.common.bean.Result;
import com.poemSys.common.utils.JwtUtils;
import com.poemSys.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler
{
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {
        Result result = new Result(0, "退出登录成功", null);

        if (authentication != null)
        {
            //把redis缓存的登录用户信息清除
            String userId = authentication.getName();

            boolean isOk = redisUtil.del("LoginSysUser:" + userId);
            if (!isOk)
            {
                result.setCode(-1);
                result.setMsg("服务器异常(redis)，退出登录失败");
            }
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();

        response.setHeader(jwtUtils.getHeader(), "");

        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));

        outputStream.flush();
        outputStream.close();

    }
}
