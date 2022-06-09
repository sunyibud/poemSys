package com.poemSys.user.service.forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysComment;
import com.poemSys.common.entity.connection.ConPostComment;
import com.poemSys.common.service.ConPostCommentService;
import com.poemSys.common.service.SysCommentService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeleteMyCommentService
{
    @Autowired
    SysCommentService sysCommentService;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    ConPostCommentService conPostCommentService;

    public Result delete(IdForm idForm)
    {
        Long userId = getLoginSysUserService.getSysUser().getId();
        long commentId = idForm.getId();
        SysComment sysComment = sysCommentService.getById(commentId);

        if(sysComment==null)
            return new Result(1, "评论不存在,id:"+commentId, null);

        if(sysComment.getOwnerUserId()!=userId)
            return new Result(-2, "权限不足,只能删除本人的评论,id:"+commentId, null);

        if(sysComment.getType()==1) //为一级评论时还需删除所有子评论
        {
            List<SysComment> list = sysCommentService.list(new QueryWrapper<SysComment>()
                    .eq("type", 2).eq("post_or_father", commentId));
            if(!list.isEmpty()) //删除子评论
                list.forEach(l-> sysCommentService.removeById(l));
        }
        ConPostComment conPostComment = conPostCommentService.getOne(new QueryWrapper<ConPostComment>()
                .eq("comment_id", commentId));
        if(conPostComment!=null)
            conPostCommentService.removeById(conPostComment);
        sysCommentService.removeById(commentId);

        return new Result(0, "评论删除成功,id:"+commentId, null);
    }
}
