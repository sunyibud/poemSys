package com.poemSys.user.service.forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysComment;
import com.poemSys.common.entity.basic.SysMessage;
import com.poemSys.common.entity.connection.ConUserPost;
import com.poemSys.common.service.ConUserPostService;
import com.poemSys.common.service.SysCommentService;
import com.poemSys.common.service.SysMessageService;
import com.poemSys.common.service.general.GetLoginSysUserService;
import com.poemSys.user.bean.Form.CommentOpeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CommentOpeService
{
    @Autowired
    SysCommentService sysCommentService;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    ConUserPostService conUserPostService;

    @Autowired
    SysMessageService sysMessageService;

    public Result comment(CommentOpeForm commentOpeForm)
    {
        Long userId = getLoginSysUserService.getSysUser().getId();
        int type = commentOpeForm.getType();
        String content = commentOpeForm.getContent();
        long id = commentOpeForm.getId(); //type为1时表示postId, 2表示commentId
        sysCommentService.save(new SysComment(userId, content,
                LocalDateTime.now(), type, id));
        //接受消息设置
        if(type==1)
        {
            long postOwnerId = conUserPostService.getOne(new QueryWrapper<ConUserPost>()
                    .eq("post_id", id)).getUserId();
            sysMessageService.save(new SysMessage(postOwnerId, false, 1, id, 0, content, 0,
                    true, LocalDateTime.now(), UUID.randomUUID().toString(), 0));
        }
        if(type==2)
        {
            SysComment fatherComment = sysCommentService.getById(id);
            long fatherCommentOwnerId = fatherComment.getOwnerUserId();
            sysMessageService.save(new SysMessage(fatherCommentOwnerId, false, 1, id,
                    0, content, 0, true, LocalDateTime.now(),
                    UUID.randomUUID().toString(), userId));
        }
        //websocket

        return new Result(0, "用户"+userId+"(id)评论提交成功", null);
    }
}
