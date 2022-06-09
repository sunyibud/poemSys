package com.poemSys.user.service.general;

import cn.hutool.json.JSONUtil;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.service.SysUserService;
import com.poemSys.common.utils.JwtUtils;
import com.poemSys.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 更改了登录用户信息后对redis中缓存的登录用户信息进行更新
 */
@Service
public class UpdateRedisSysUserService
{
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    /**
     * 更新当前登录用户
     */
    public void update()
    {
        Long userId = getLoginSysUserService.getSysUser().getId();
        SysUser sysUser = sysUserService.getById(userId);

        redisUtil.set("sysUser:" + userId,
                JSONUtil.toJsonStr(sysUser), 60 * 60 * 24 * 10);//10天
    }
    public void updateById(long userId)
    {
        SysUser sysUser = sysUserService.getById(userId);

        //将对象转化成json再存入redis
        redisUtil.set("sysUser:" + userId,
                JSONUtil.toJsonStr(sysUser), 60 * 60 * 24 * 10);//10天
    }
}
