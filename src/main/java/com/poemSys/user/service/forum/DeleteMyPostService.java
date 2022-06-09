package com.poemSys.user.service.forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPost;
import com.poemSys.common.entity.connection.ConPostComment;
import com.poemSys.common.entity.connection.ConUserPost;
import com.poemSys.common.entity.connection.ConUserPostCollect;
import com.poemSys.common.entity.connection.ConUserPostLike;
import com.poemSys.common.service.*;
import com.poemSys.user.service.general.GetLoginSysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeleteMyPostService
{
    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    ConUserPostService conUserPostService;

    @Autowired
    SysPostService sysPostService;

    @Autowired
    ConPostCommentService conPostCommentService;

    @Autowired
    SysCommentService sysCommentService;

    @Autowired
    ConUserPostLikeService conUserPostLikeService;

    @Autowired
    ConUserPostCollectService conUserPostCollectService;

    public Result delete(IdForm idForm)
    {
        Long userId = getLoginSysUserService.getSysUser().getId();
        long postId = idForm.getId();

        SysPost sysPost = sysPostService.getById(postId);
        if(sysPost==null)
            return new Result(1, "帖子不存在,id:"+postId, null);

        ConUserPost con = conUserPostService.getOne(new QueryWrapper<ConUserPost>()
                .eq("user_id", userId).eq("post_id", postId));
        if(con==null)
            return new Result(-2, "权限不足,只能删除自己的帖子,id:"+postId, null);

        //删除与该帖子以及与该帖子有关的所有关联表
        conUserPostService.removeById(con);
        List<ConUserPostLike> conUserPostLikeList = conUserPostLikeService.list(new QueryWrapper<ConUserPostLike>()
                .eq("post_id", postId));
        if(!conUserPostLikeList.isEmpty())
            conUserPostLikeList.forEach(c-> conUserPostLikeService.removeById(c));
        List<ConUserPostCollect> conUserPostCollectList = conUserPostCollectService.list(new QueryWrapper<ConUserPostCollect>()
                .eq("post_id", postId));
        if(!conUserPostCollectList.isEmpty())
            conUserPostCollectList.forEach(c-> conUserPostCollectService.removeById(c));

        //删除该帖子下的所有评论
        List<ConPostComment> con1 = conPostCommentService.list(new QueryWrapper<ConPostComment>()
                .eq("post_id", postId));
        con1.forEach(c->{
            //只需要删除评论,帖子评论关联表中的字段会通过触发器删除
            sysCommentService.removeById(c.getCommentId());
        });

        sysPostService.removeById(postId);

        return new Result(0, "帖子删除成功,id"+postId, null);
    }
}
