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
import com.poemSys.user.bean.WebsocketMsg;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.user.bean.Form.AddCommentForm;
import com.poemSys.user.service.general.WebSocketService;
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

    @Autowired
    ContentCheckService contentCheckService;

    @Autowired
    WebSocketService webSocketService;

    public Result comment(AddCommentForm addCommentForm)
    {
        Long loginUserId = getLoginSysUserService.getSysUser().getId();
        int type = addCommentForm.getType();
        String content = addCommentForm.getContent();
        long postId = addCommentForm.getPostId();
        long commentId = addCommentForm.getCommentId();//type为2时不起作用

        if(type!=1&&type!=2)
            return new Result(-3, "评论类型格式不正确1~2", null);

        if(type==1)
        {
            SysPost sysPost = sysPostService.getById(postId);
            if (sysPost == null)
                return new Result(0, "评论帖子不存在,id:" + postId, null);
        }
        if(type==2)
        {
            SysComment fatherComment = sysCommentService.getById(commentId);
            if (fatherComment == null)
                return new Result(0, "评论失败,父评论不存在,id:" + commentId, null);
        }

        //敏感词替换
        content = contentCheckService.KMPReplace(content);

        //保存评论
        String uuid = UUID.randomUUID().toString();
        if(type==1)
            sysCommentService.save(new SysComment(loginUserId, content,
                    LocalDateTime.now(), type, postId, uuid));
        else sysCommentService.save(new SysComment(loginUserId, content,
                LocalDateTime.now(), type, commentId, uuid));

        //将评论和帖子关联
        SysComment sysComment = sysCommentService.getOne(new QueryWrapper<SysComment>()
                .eq("uuid", uuid));

        conPostCommentService.save(new ConPostComment(postId, sysComment.getId()));

        //推送消息
        if(type==1)
        {
            long postOwnerId = conUserPostService.getOne(new QueryWrapper<ConUserPost>()
                    .eq("post_id", postId)).getUserId();
            if(postOwnerId!=loginUserId)//当评论自己的帖子时不推送
                sysMessageService.save(new SysMessage(postOwnerId, false, 1, postId, sysComment.getId()+","+content,
                        loginUserId, true, LocalDateTime.now(), UUID.randomUUID().toString(), 0));
            webSocketService.sendMessageOnServer(postOwnerId,
                    new WebsocketMsg(1, 0, postOwnerId, "用户"+loginUserId+"评论了你的帖子"+postId));

        }
        else
        {
            SysComment fatherComment = sysCommentService.getById(commentId);
            long fatherCommentOwnerId = fatherComment.getOwnerUserId();
            if(fatherCommentOwnerId!=loginUserId)//当评论自己的评论时不推送
                sysMessageService.save(new SysMessage(fatherCommentOwnerId, false, 1, postId,
                        sysComment.getId()+","+content, loginUserId, true, LocalDateTime.now(),
                    UUID.randomUUID().toString(), commentId));
            webSocketService.sendMessageOnServer(fatherCommentOwnerId,
                    new WebsocketMsg(1, 0, fatherCommentOwnerId, "用户"+fatherCommentOwnerId+"评论了你的评论"+commentId));
        }
        //websocket

        return new Result(0, "用户"+loginUserId+"(id)评论提交成功", null);
    }
}
