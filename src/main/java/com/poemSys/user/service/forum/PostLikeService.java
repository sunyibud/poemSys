package com.poemSys.user.service.forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysMessage;
import com.poemSys.common.entity.basic.SysPost;
import com.poemSys.common.entity.connection.ConUserPost;
import com.poemSys.common.entity.connection.ConUserPostLike;
import com.poemSys.common.service.ConUserPostLikeService;
import com.poemSys.common.service.ConUserPostService;
import com.poemSys.common.service.SysMessageService;
import com.poemSys.common.service.SysPostService;
import com.poemSys.common.service.general.GetLoginSysUserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PostLikeService
{
    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    ConUserPostLikeService conUserPostLikeService;

    @Autowired
    SysPostService sysPostService;

    @Autowired
    ConUserPostService conUserPostService;

    @Autowired
    SysMessageService sysMessageService;

    public Result like(IdForm idForm)
    {
        Long userId = getLoginSysUserService.getSysUser().getId();
        long postId = idForm.getId();

        SysPost sysPost = sysPostService.getById(postId);
        if(sysPost==null)
            return new Result(1, "帖子不存在,id:"+postId, null);

        ConUserPostLike con = conUserPostLikeService.getOne(new QueryWrapper<ConUserPostLike>()
                .eq("user_id", userId).eq("post_id", postId));
        boolean isLike = false;
        if(con==null)   //点赞
        {
            conUserPostLikeService.save(new ConUserPostLike(userId, postId));
            isLike = true;
            //推送消息
            ConUserPost con1 = conUserPostService.getOne(new QueryWrapper<ConUserPost>()
                    .eq("user_id", userId).eq("post_id", postId));
            long postOwnerId = con1.getUserId();
            sysMessageService.save(new SysMessage(postOwnerId, false, 3, postId, 0, "用户点赞了你的帖子", 0,
                    true, LocalDateTime.now(), UUID.randomUUID().toString(), 0));
        }
        else
            conUserPostLikeService.removeById(con);

        int likeNum = sysPostService.getById(postId).getLikeNum();
        NewState newState = new NewState(isLike, likeNum);
        return new Result(0, "用户"+userId+"(id)点赞/取消点赞帖子"+postId+"(id)成功", newState);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class NewState implements Serializable
    {
        private static final long serialVersionUID = 1L;

        private boolean isLike;
        private int likeNum;
    }
}
