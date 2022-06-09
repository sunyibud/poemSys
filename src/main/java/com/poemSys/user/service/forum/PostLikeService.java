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
import com.poemSys.user.bean.WebsocketMsg;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.user.service.general.WebSocketService;
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

    @Autowired
    WebSocketService webSocketService;

    public Result like(IdForm idForm)
    {
        Long loginUserId = getLoginSysUserService.getSysUser().getId();
        long postId = idForm.getId();

        SysPost sysPost = sysPostService.getById(postId);
        if(sysPost==null)
            return new Result(1, "帖子不存在,id:"+postId, null);

        ConUserPostLike con = conUserPostLikeService.getOne(new QueryWrapper<ConUserPostLike>()
                .eq("user_id", loginUserId).eq("post_id", postId));
        boolean isLike = false;
        if(con==null)   //帖子点赞
        {
            conUserPostLikeService.save(new ConUserPostLike(loginUserId, postId));
            isLike = true;
            //推送消息
            ConUserPost con1 = conUserPostService.getOne(new QueryWrapper<ConUserPost>()
                    .eq("post_id", postId));
            long postOwnerId = con1.getUserId();
            //（如果存在同一个用户用户为同一个帖子的点赞消息，无需新建，改变receiveTime和state）
            SysMessage one = sysMessageService.getOne(new QueryWrapper<SysMessage>()
                    .eq("user_id", postOwnerId).eq("type", 3)
                    .eq("other_user_id", loginUserId)
                    .eq("post_id", postId).eq("content", "用户点赞了你的帖子"));
            if(one==null)
                sysMessageService.save(new SysMessage(postOwnerId, false, 3, postId, "用户点赞了你的帖子",
                        loginUserId, true, LocalDateTime.now(), UUID.randomUUID().toString(), 0));
            else
            {
                one.setReceiveTime(LocalDateTime.now());
                one.setState(false);
                sysMessageService.updateById(one);
            }
            webSocketService.sendMessageOnServer(postOwnerId, new WebsocketMsg(3, 0, postOwnerId, "用户"+loginUserId+"点赞了你的帖子"));

        }
        else    //取消点赞
            conUserPostLikeService.removeById(con);

        int likeNum = sysPostService.getById(postId).getLikeNum();
        NewState newState = new NewState(isLike, likeNum);
        return new Result(0, "用户"+loginUserId+"(id)点赞/取消点赞帖子"+postId+"(id)成功", newState);
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
