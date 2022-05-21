package com.poemSys.user.service.general;

import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.service.general.GetLoginSysUserService;
import com.poemSys.common.utils.JwtUtils;
import com.poemSys.user.bean.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class GetUserInfoByJwtService
{
    @Autowired
    HttpServletRequest request;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    public UserInfo getUserInfo()
    {
        SysUser sysUser = getLoginSysUserService.getSysUser();

        return new UserInfo(sysUser.getUsername(), sysUser.getSignature(),
                sysUser.getSex(), sysUser.getEmail(), sysUser.getTelephone(),
                sysUser.getHeadPath(), sysUser.getFollowNum(), sysUser.getFansNum());
    }
}
