package com.poemSys.common.security;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poemSys.common.bean.Const;
import com.poemSys.common.bean.IsOk;
import com.poemSys.common.bean.Result;
import com.poemSys.common.exception.CaptchaException;
import com.poemSys.common.utils.JwtUtils;
import com.poemSys.common.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 重写登录认证过滤器
 */
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter
{
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Autowired
    JwtUtils jwtUtils;

    private String username;
    private String password;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication
            (HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
    {
        log.info("登录验证");
        //判断请求方法是否是post
        if (!"POST".equals(request.getMethod()))
        {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else
        {
            try
            {
                //先判断本次登录操作是否已经携带jwt且jwt合法（已经登录）
                String jwt = request.getHeader(jwtUtils.getHeader());
                Claims claim = jwtUtils.getClaimByToken(jwt);
                if(claim!=null)
                {
                    log.error("已经登录");
                    throw new CaptchaException("您已经登录，无需重复登录");//将自定义异常抛出给认证失败处理器
                }

                Map<String, String> map;
                //转换输入的json数据为map格式
                map = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                String key = map.get("key");
                String code = map.get("code");
                this.username = map.get(getUsernameParameter());
                this.password = map.get(getPasswordParameter());

                if (StringUtils.isBlank(code) || StringUtils.isBlank(key))
                {
                    log.error("验证码为空");
                    throw new CaptchaException("验证码错误");//将自定义异常抛出给认证失败处理器
                }
                IsOk isOk = new IsOk();
                Object object = redisUtil.hget(Const.CAPTCHA_KEY, key, isOk);
                if(!isOk.isOk())
                    throw new CaptchaException("服务器异常, 验证码验证失败");

                if (!code.equals(object))
                {
                    log.error("验证码错误");
                    throw new CaptchaException("验证码错误");
                }

                //一次性使用，在Redis中去掉验证码
                if(!redisUtil.hdel(Const.CAPTCHA_KEY, key))
                    throw new CaptchaException("服务器异常, 验证码验证失败");

            } catch (CaptchaException e)
            {
                // 交给认证失败处理器
                loginFailureHandler.onAuthenticationFailure(request, response, e);
            } catch (JsonParseException e)
            {
                log.error("登录接受json格式有误");
                ServletOutputStream outputStream = response.getOutputStream();
                Result result = new Result(-3, "请求体json格式有误", e.getMessage());
                outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();
            }
            UsernamePasswordAuthenticationToken authRequest;
            authRequest = new UsernamePasswordAuthenticationToken(username, password);
            //发送给UserDetailsService信息
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }
}

