package com.poemSys.user.service.forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPost;
import com.poemSys.common.entity.connection.ConUserPost;
import com.poemSys.common.service.ConUserPostService;
import com.poemSys.common.service.SysPostService;
import com.poemSys.user.bean.UserInfo;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.user.bean.SysPostRes;
import com.poemSys.user.service.general.SwapSysPostRecService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;

@Service
public class GetPostByIdService
{
    @Autowired
    SysPostService sysPostService;

    @Autowired
    ConUserPostService conUserPostService;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    SwapSysPostRecService swapSysPostRecService;

    public Result get(IdForm idForm)
    {
        Long userId = getLoginSysUserService.getSysUser().getId();
        long postId = idForm.getId();

        SysPost sysPost = sysPostService.getById(postId);
        if (sysPost == null)
            return new Result(1, "帖子不存在,id:"+postId, null);

        ConUserPost con = conUserPostService.getOne(new QueryWrapper<ConUserPost>()
                .eq("user_id", userId).eq("post_id", postId));
        boolean isOwner = false;
        if(con!=null)
            isOwner = true;

        SysPostRes res = swapSysPostRecService.swap(sysPost);
        FinalRes finalRes = new FinalRes(res.getId(), res.getOwnerUserInfo(), res.getTitle(),
                res.getContent(), res.getCreatedTime(), res.getCollectNum(), res.getLikeNum(),
                res.getCoverImage(), res.isLike(), res.isCollect(), isOwner);
        return new Result(0, "帖子信息获取成功，id:" + res.getId(), finalRes);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class FinalRes implements Serializable
    {
        protected static final long serialVersionUID = 1L;

        protected long id;
        private UserInfo ownerUserInfo;
        protected String title;
        protected String content;
        protected LocalDateTime createdTime;
        protected int collectNum;
        protected int likeNum;
        protected String coverImage;

        protected boolean isLike;
        protected boolean isCollect;

        private boolean isOwner;
    }
}
