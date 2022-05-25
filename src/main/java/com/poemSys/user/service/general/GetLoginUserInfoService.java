package com.poemSys.user.service.general;

import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.user.bean.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetLoginUserInfoService
{
    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    SwapUserInfoService swapUserInfoService;

    public UserInfo getUserInfo()
    {
        SysUser sysUser = getLoginSysUserService.getSysUser();
        return swapUserInfoService.swap(sysUser);
    }
}
