package com.poemSys.user.service.general;

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
 * 通过id获取sysUser（redis优化）
 */
@Service
public class GetLoginSysUserService
{
    @Autowired
    HttpServletRequest request;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    JwtUtils jwtUtils;

    public SysUser getSysUser()
    {
        //从jwt中取出用户id
        String jwt = request.getHeader(jwtUtils.getHeader());
        Claims claim = jwtUtils.getClaimByToken(jwt);
        if(claim==null)
            return null;
        Long id = Long.parseLong(claim.getSubject());


        //查看redis中是否缓存
        if(redisUtil.hasKey("LoginSysUser:"+id))
        {
            String jsonStr = (String) redisUtil.get("LoginSysUser:" + id);
            return JSONUtil.toBean(jsonStr, SysUser.class);
        }
        else
        {
            SysUser sysUser = sysUserService.getSysUserById(id);
            //将对象转化成json再存入redis
            redisUtil.set("LoginUserInfo:" + id,
                    JSONUtil.toJsonStr(sysUser), 60 * 60);//60分钟
            return sysUser;
        }
    }
}
