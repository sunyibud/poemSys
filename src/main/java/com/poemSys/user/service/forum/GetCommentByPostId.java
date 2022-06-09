package com.poemSys.user.service.forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysComment;
import com.poemSys.common.entity.connection.ConPostComment;
import com.poemSys.common.service.ConPostCommentService;
import com.poemSys.common.service.SysCommentService;
import com.poemSys.common.service.SysPostService;
import com.poemSys.common.service.SysUserService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.user.bean.LevelOneComment;
import com.poemSys.user.bean.SonComment;
import com.poemSys.user.service.general.SwapUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetCommentByPostId
{
    @Autowired
    SysPostService sysPostService;

    @Autowired
    ConPostCommentService conPostCommentService;

    @Autowired
    SysCommentService sysCommentService;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SwapUserInfoService swapUserInfoService;

    public Result get(IdForm idForm)
    {
        Long userId = getLoginSysUserService.getSysUser().getId();
        long postId = idForm.getId();

        if(sysPostService.getById(postId)==null)
            return new Result(1, "帖子不存在,id:"+postId, null);

        //该帖子下所有的评论
        List<ConPostComment> con = conPostCommentService.list(new QueryWrapper<ConPostComment>()
                .eq("post_id", postId));
        List<Long> commentIds = new ArrayList<>();
        con.forEach(c-> commentIds.add(c.getCommentId()));
        if(commentIds.isEmpty())
            return new Result(0, "当前帖子还没有评论,id:"+postId, null);

        List<SysComment> comments = sysCommentService.list(new QueryWrapper<SysComment>()
                .in("id", commentIds));


        List<LevelOneComment> res = new ArrayList<>();
        Map<Integer, List<Long>> map = new HashMap<>();   //res下标与某一级评论(包含本身)及其下的所有二级评论id列表映射的哈希表
        comments.forEach(c->{//将父评论加入到返回结果列表中
            if(c.getType()==1)
            {
                boolean isOwner = false;
                if(c.getOwnerUserId()==userId)
                    isOwner = true;
                List<Long> idList = new ArrayList<>();
                idList.add(c.getId());
                map.put(res.size(), idList);
                res.add(new LevelOneComment(c.getId(),
                        swapUserInfoService.swap(sysUserService.getById(c.getOwnerUserId())),
                        c.getContent(), c.getCreatedTime(), isOwner, new ArrayList<>()));
            }
        });
        comments.forEach(c->{//添加子评论到返回结果列表里父评论的List中
            if(c.getType()==2)
            {
                long fatherCommentId = c.getPostOrFather();
                long fatherCommentOwnerUserId = sysCommentService.getById(fatherCommentId).getOwnerUserId();
                map.forEach((ind, list)->{
                    if(list.contains(fatherCommentId))
                    {
                        list.add(c.getId());
                        LevelOneComment levelOneComment = res.get(ind);
                        boolean isOwner = false;
                        if(c.getOwnerUserId()==userId)
                            isOwner = true;
                        levelOneComment.getSonComments().add(new SonComment(c.getId(),
                                swapUserInfoService.swap(sysUserService.getById(c.getOwnerUserId())),
                                swapUserInfoService.swap(sysUserService.getById(fatherCommentOwnerUserId)),
                                c.getContent(), c.getCreatedTime(), isOwner));
                    }
                });
            }
        });
        return new Result(0, "帖子"+postId+"(id)的评论获取成功", res);
    }
}
