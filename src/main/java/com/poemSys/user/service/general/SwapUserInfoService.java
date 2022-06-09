package com.poemSys.user.service.general;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.entity.connection.ConUserFollow;
import com.poemSys.common.entity.connection.ConUserPost;
import com.poemSys.common.service.ConUserFollowService;
import com.poemSys.common.service.ConUserPostService;
import com.poemSys.user.bean.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 转化sysUser为UserInfo
 */
@Service
public class SwapUserInfoService
{
    @Autowired
    ConUserPostService conUserPostService;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    ConUserFollowService conUserFollowService;

    /**
     * @param sysUser 被转化的sysUser
     * @return 传入的sysUser被转化后的UserInfo
     */
    public UserInfo swap(SysUser sysUser)
    {
        if(sysUser == null)
            return null;

        Long loginUserId = getLoginSysUserService.getSysUser().getId();
        Long userId = sysUser.getId();

        ConUserFollow one = conUserFollowService.getOne(new QueryWrapper<ConUserFollow>()
                .eq("follow_user_id", loginUserId)
                .eq("be_follow_user_id", userId));
        boolean isFollow = false;
        if(one!=null)
            isFollow = true;

        int countNum = conUserPostService.count(new QueryWrapper<ConUserPost>()
                .eq("user_id", userId));
        return new UserInfo(userId, sysUser.getUsername(),
                sysUser.getSignature(), sysUser.getSex(), sysUser.getEmail(),
                sysUser.getTelephone(), sysUser.getHeadPath(),
                sysUser.getFollowNum(), sysUser.getFansNum(), countNum, isFollow);
    }
}
