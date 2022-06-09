package com.poemSys.user.service.userInfo;

import com.poemSys.common.entity.basic.SysLetter;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.service.SysUserService;
import com.poemSys.user.bean.SysLetterRes;
import com.poemSys.user.bean.UserInfo;
import com.poemSys.user.service.general.SwapUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *sysLetter转sysLetterRes
 */
@Service
public class SwapSysLetterResService
{
    @Autowired
    SwapUserInfoService swapUserInfoService;

    @Autowired
    SysUserService sysUserService;

    public SysLetterRes swap(SysLetter sysLetter)
    {
        SysUser sendUser = sysUserService.getSysUserById(sysLetter.getSendUserId());
        UserInfo sendUserInfo = swapUserInfoService.swap(sendUser);
        SysUser receiveUser = sysUserService.getSysUserById(sysLetter.getReceiveUserId());
        UserInfo receiveUserInfo = swapUserInfoService.swap(receiveUser);

        return new SysLetterRes(sendUserInfo, receiveUserInfo, sysLetter.getContent(),
                sysLetter.getTime(), sysLetter.isState());
    }
}
