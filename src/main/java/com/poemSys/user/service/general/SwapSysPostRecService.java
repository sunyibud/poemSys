package com.poemSys.user.service.general;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.common.entity.basic.SysPost;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.entity.connection.ConUserPost;
import com.poemSys.common.entity.connection.ConUserPostCollect;
import com.poemSys.common.entity.connection.ConUserPostLike;
import com.poemSys.common.service.ConUserPostCollectService;
import com.poemSys.common.service.ConUserPostLikeService;
import com.poemSys.common.service.ConUserPostService;
import com.poemSys.common.service.SysUserService;
import com.poemSys.user.bean.SysPostRes;
import com.poemSys.user.bean.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SwapSysPostRecService
{
    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    ConUserPostLikeService conUserPostLikeService;

    @Autowired
    ConUserPostCollectService conUserPostCollectService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    ConUserPostService conUserPostService;

    @Autowired
    SwapUserInfoService swapUserInfoService;

    public SysPostRes swap(SysPost sysPost)
    {
        boolean isLike = false, isCollect = false;
        SysUser sysUser = getLoginSysUserService.getSysUser();
        if(sysUser!=null)
        {
            long userId = sysUser.getId();
            ConUserPostLike conLike = conUserPostLikeService.getOne(new QueryWrapper<ConUserPostLike>()
                    .eq("user_id", userId).eq("post_id", sysPost.getId()));
            ConUserPostCollect conCollect = conUserPostCollectService.getOne(new QueryWrapper<ConUserPostCollect>()
                    .eq("user_id", userId).eq("post_id", sysPost.getId()));
            if (conLike != null)
                isLike = true;
            if (conCollect != null)
                isCollect = true;
        }

        ConUserPost con = conUserPostService.getOne(new QueryWrapper<ConUserPost>()
                .eq("post_id", sysPost.getId()));
        SysUser postUser = sysUserService.getById(con.getUserId());
        UserInfo postUserInfo = swapUserInfoService.swap(postUser);

        return new SysPostRes(sysPost.getId(), postUserInfo, sysPost.getTitle(), sysPost.getContent(),
                sysPost.getCreatedTime(), sysPost.getCollectNum(), sysPost.getLikeNum(),
                sysPost.getCoverImage(), isLike, isCollect);
    }
}
