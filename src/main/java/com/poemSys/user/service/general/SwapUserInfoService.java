package com.poemSys.user.service.general;

import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.user.bean.UserInfo;
import org.springframework.stereotype.Service;

/**
 * 转化sysUser为UserInfo
 */
@Service
public class SwapUserInfoService
{
    public UserInfo swap(SysUser sysUser)
    {
        return new UserInfo(sysUser.getId(), sysUser.getUsername(),
                sysUser.getSignature(), sysUser.getSex(), sysUser.getEmail(),
                sysUser.getTelephone(), sysUser.getHeadPath(),
                sysUser.getFollowNum(), sysUser.getFansNum());
    }
}
