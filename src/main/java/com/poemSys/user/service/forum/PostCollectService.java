package com.poemSys.user.service.forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysMessage;
import com.poemSys.common.entity.basic.SysPost;
import com.poemSys.common.entity.connection.ConUserPost;
import com.poemSys.common.entity.connection.ConUserPostCollect;
import com.poemSys.common.entity.connection.ConUserPostLike;
import com.poemSys.common.service.ConUserPostCollectService;
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
public class PostCollectService
{
    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    SysPostService sysPostService;

    @Autowired
    ConUserPostCollectService conUserPostCollectService;

    @Autowired
    SysMessageService sysMessageService;

    @Autowired
    ConUserPostService conUserPostService;

    public Result collect(IdForm idForm)
    {
        Long userId = getLoginSysUserService.getSysUser().getId();
        long postId = idForm.getId();

        SysPost sysPost = sysPostService.getById(postId);
        if(sysPost==null)
            return new Result(1, "帖子不存在,id:"+postId, null);

        ConUserPostCollect con = conUserPostCollectService.getOne(new QueryWrapper<ConUserPostCollect>()
                .eq("user_id", userId).eq("post_id", postId));
        boolean isCollect = false;
        if(con==null)   //收藏
        {
            conUserPostCollectService.save(new ConUserPostCollect(userId, postId));
            isCollect = true;
            //推送消息
            ConUserPost con1 = conUserPostService.getOne(new QueryWrapper<ConUserPost>()
                    .eq("user_id", userId).eq("post_id", postId));
            long postOwnerId = con1.getUserId();
            sysMessageService.save(new SysMessage(postOwnerId, false, 3, postId, 0, "用户收藏了你的帖子", 0,
                    true, LocalDateTime.now(), UUID.randomUUID().toString(), 0));
        }
        else
            conUserPostCollectService.removeById(con);

        int collectNum = sysPostService.getById(postId).getCollectNum();
        NewState newState = new NewState(isCollect, collectNum);
        return new Result(0, "用户"+userId+"(id)收藏/取消收藏帖子"+postId+"(id)成功", newState);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class NewState implements Serializable
    {
        private static final long serialVersionUID = 1L;

        private boolean isCollect;
        private int collectNum;
    }
}
