package com.poemSys.user.service.forum;

import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysComment;
import com.poemSys.common.service.SysCommentService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteMyCommentService
{
    @Autowired
    SysCommentService sysCommentService;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    public Result delete(IdForm idForm)
    {
        Long userId = getLoginSysUserService.getSysUser().getId();
        long commentId = idForm.getId();
        SysComment sysComment = sysCommentService.getById(commentId);

        if(sysComment==null)
            return new Result(1, "评论不存在,id:"+commentId, null);

        if(sysComment.getOwnerUserId()!=userId)
            return new Result(-2, "权限不足,只能删除本人的评论,id:"+commentId, null);

        if(sysComment.getType()==1)
        {
            sysComment.setContent("已删除");
            sysCommentService.updateById(sysComment);
            return new Result(0, "评论删除成功,id:"+commentId, null);
        }
        //只需要直接删除，通过触发器删除帖子关联表中的字段
        sysCommentService.removeById(commentId);
        return new Result(0, "评论删除成功", null);
    }
}
