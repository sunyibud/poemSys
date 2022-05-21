package com.poemSys.common.security;

import cn.hutool.json.JSONUtil;
import com.poemSys.common.bean.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * jwt权限不足
 */
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler
{
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException
    {
        response.setContentType("application/json;charset=UTF-8");
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);//权限不足状态码(403)

        ServletOutputStream outputStream = response.getOutputStream();

        log.error(accessDeniedException.getMessage());

        Result result = new Result(-2, accessDeniedException.getMessage(), null);

        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));

        outputStream.flush();
        outputStream.close();
    }
}
