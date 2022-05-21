package com.poemSys.common.security;

import cn.hutool.json.JSONUtil;
import com.poemSys.common.bean.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 登录失败处理器
 */
@Slf4j
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();

        String msg = exception.getMessage();
        log.error(msg);
        int code=1;
        if(exception.getMessage().equals("Bad credentials"))
            msg="用户名或密码有误";
        else if(exception.getMessage().equals("服务器异常, 验证码验证失败"))
        {
            code = -1;
        }
        else if(exception instanceof LockedException)
        {
            code = -2;
        }
        Result result = new Result(code, msg, null);

        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));

        outputStream.flush();
        outputStream.close();
    }
}