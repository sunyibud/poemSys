package com.poemSys.user.service.general;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.common.entity.basic.SysPost;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.entity.connection.ConUserPostCollect;
import com.poemSys.common.entity.connection.ConUserPostLike;
import com.poemSys.common.service.ConUserPostCollectService;
import com.poemSys.common.service.ConUserPostLikeService;
import com.poemSys.common.service.general.GetLoginSysUserService;
import com.poemSys.user.bean.SysPostRes;
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
        return new SysPostRes(sysPost.getId(), sysPost.getTitle(), sysPost.getContent(),
                sysPost.getCreatedTime(), sysPost.getCollectNum(), sysPost.getLikeNum(),
                sysPost.getCoverImage(), isLike, isCollect);
    }
}
