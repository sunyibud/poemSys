package com.poemSys.user.service.forum;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysComment;
import com.poemSys.common.entity.basic.SysMessage;
import com.poemSys.common.entity.basic.SysPost;
import com.poemSys.common.entity.connection.ConPostComment;
import com.poemSys.common.entity.connection.ConUserPost;
import com.poemSys.common.service.*;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.user.bean.Form.AddCommentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AddCommentService
{
    @Autowired
    SysCommentService sysCommentService;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    ConUserPostService conUserPostService;

    @Autowired
    SysMessageService sysMessageService;

    @Autowired
    SysPostService sysPostService;

    @Autowired
    ConPostCommentService conPostCommentService;

    public Result comment(AddCommentForm addCommentForm)
    {
        Long userId = getLoginSysUserService.getSysUser().getId();
        int type = addCommentForm.getType();
        String content = addCommentForm.getContent();
        long id = addCommentForm.getId(); //type为1时表示postId, 2表示commentId

        //推送消息
        if(type==1)
        {
            SysPost sysPost = sysPostService.getById(id);
            if(sysPost==null)
                return new Result(0, "评论帖子不存在,id:"+id, null);

            long postOwnerId = conUserPostService.getOne(new QueryWrapper<ConUserPost>()
                    .eq("post_id", id)).getUserId();
            sysMessageService.save(new SysMessage(postOwnerId, false, 1, id, 0, content, 0,
                    true, LocalDateTime.now(), UUID.randomUUID().toString(), 0));
        }
        if(type==2)
        {
            SysComment fatherComment = sysCommentService.getById(id);
            if(fatherComment==null)
                return new Result(0, "一级评论不存在,id:"+id, null);
            if(fatherComment.getType()==2)
                return new Result(0, "该id不是一级评论id:"+id, null);
            long fatherCommentOwnerId = fatherComment.getOwnerUserId();
            sysMessageService.save(new SysMessage(fatherCommentOwnerId, false, 1, id,
                    0, content, 0, true, LocalDateTime.now(),
                    UUID.randomUUID().toString(), userId));
        }

        //保存评论
        String uuid = UUID.randomUUID().toString();
        sysCommentService.save(new SysComment(userId, content,
                LocalDateTime.now(), type, id, uuid));
        SysComment sysComment = sysCommentService.getOne(new QueryWrapper<SysComment>()
                .eq("uuid", uuid));

        //将评论和帖子关联
        long postId = id;
        if(type == 2)
        {
            ConPostComment con = conPostCommentService.getOne(new QueryWrapper<ConPostComment>()
                    .eq("comment_id", id));
            postId = con.getPostId();
        }
        conPostCommentService.save(new ConPostComment(postId, sysComment.getId()));

        //websocket

        return new Result(0, "用户"+userId+"(id)评论提交成功", null);
    }
}
