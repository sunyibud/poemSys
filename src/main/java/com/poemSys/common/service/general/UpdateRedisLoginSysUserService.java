package com.poemSys.common.service.general;

import cn.hutool.json.JSONUtil;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.service.SysUserService;
import com.poemSys.common.utils.JwtUtils;
import com.poemSys.common.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 更改了登录用户信息后对redis中缓存的登录用户信息进行更新
 */
@Service
public class UpdateRedisLoginSysUserService
{
    @Autowired
    HttpServletRequest request;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    RedisUtil redisUtil;

    public void update()
    {
        //从jwt中取出用户id
        String jwt = request.getHeader(jwtUtils.getHeader());
        Claims claim = jwtUtils.getClaimByToken(jwt);
        Long id = Long.parseLong(claim.getSubject());
        SysUser sysUser = sysUserService.getSysUserById(id);

        //将对象转化成json再存入redis
        redisUtil.set("LoginUserInfo:" + id,
                JSONUtil.toJsonStr(sysUser), 60 * 60);//60分钟
    }
}
