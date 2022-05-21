package com.poemSys.user.service.forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysComment;
import com.poemSys.common.entity.connection.ConPostComment;
import com.poemSys.common.service.ConPostCommentService;
import com.poemSys.common.service.SysCommentService;
import com.poemSys.user.bean.FatherComment;
import com.poemSys.user.bean.SonComment;
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
    ConPostCommentService conPostCommentService;

    @Autowired
    SysCommentService sysCommentService;

    public Result get(IdForm idForm)
    {
        long postId = idForm.getId();
        List<ConPostComment> con = conPostCommentService.list(new QueryWrapper<ConPostComment>()
                .eq("post_id", postId));
        List<Long> commentIds = new ArrayList<>();
        con.forEach(c->{
            commentIds.add(c.getCommentId());
        });
        List<SysComment> comments = sysCommentService.list(new QueryWrapper<SysComment>()
                .in("id", commentIds));
        List<FatherComment> res = new ArrayList<>();
        Map<Long, Integer> map = new HashMap<>();   //fatherComment.id与res下表映射的哈希表
        comments.forEach(c->{
            if(c.getType()==1)
            {
                map.put(c.getId(), res.size());
                res.add(new FatherComment(c.getId(), c.getOwnerUserId(), c.getContent(),
                        c.getCreatedTime(), new ArrayList<>()));
            }
        });
        comments.forEach(c->{
            if(c.getType()==2)
            {
                FatherComment fatherComment = res.get(map.get(c.getPostOrFather()));
                fatherComment.getSonComments().add(new SonComment(c.getId(),
                        c.getOwnerUserId(), fatherComment.getOwnerUserId(),
                        c.getContent(), c.getCreatedTime()));
            }
        });
        return new Result(0, "帖子"+postId+"(id)的评论获取成功", null);
    }
}
