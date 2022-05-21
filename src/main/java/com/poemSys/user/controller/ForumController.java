package com.poemSys.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPost;
import com.poemSys.common.service.SysPostService;
import com.poemSys.user.bean.Form.AddPostForm;
import com.poemSys.user.bean.Form.AddCommentForm;
import com.poemSys.user.bean.Form.UpdateMyPostForm;
import com.poemSys.user.bean.PostPageAns;
import com.poemSys.user.service.forum.*;
import com.poemSys.user.service.general.PostPageAnsPro;
import com.poemSys.user.service.general.SwapSysPostRecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 论坛模块接口
 */
@RestController
@RequestMapping("/api/forum")
public class ForumController
{
    @Autowired
    SysPostService sysPostService;

    @Autowired
    GetPostByIdService getPostByIdService;

    @Autowired
    GetCommentByPostId getCommentByPostId;

    @Autowired
    AddCommentService addCommentService;

    @Autowired
    DeleteMyCommentService deleteMyCommentService;

    @Autowired
    GetMyPostService getMyPostService;

    @Autowired
    PostPageAnsPro postPageAnsPro;

    @Autowired
    SwapSysPostRecService swapSysPostRecService;

    @Autowired
    AddPostService addPostService;

    @Autowired
    UpdateMyPostService updateMyPostService;

    @Autowired
    DeleteMyPostService deleteMyPostService;

    @Autowired
    PostLikeService postLikeService;

    @Autowired
    PostCollectService postCollectService;

    @PostMapping("/partPostList/{page}/{size}")
    public Result partPostList(@PathVariable("page") Integer page,
                               @PathVariable("size") Integer size)
    {
        Page<SysPost> postPage = new Page<>(page, size);
        Page<SysPost> pageAns = sysPostService.page(postPage);
        PostPageAns res = postPageAnsPro.pro(pageAns);
        return new Result(0, "分页获取帖子列表成功", res);
    }

    @PostMapping("/getPostById")
    public Result getPostById(IdForm idForm)
    {
        return getPostByIdService.get(idForm);
    }

    @PostMapping("/getCommentByPostId")
    public Result getCommentByPostId(IdForm idForm)
    {
        return getCommentByPostId.get(idForm);
    }

    @PostMapping("/getMyPost/{page}/{size}")
    public Result getMyPost(@PathVariable("page") Integer page,
                            @PathVariable("size") Integer size)
    {
        return getMyPostService.get(page, size);
    }

    @PostMapping("/addPost")
    public Result addPost(AddPostForm addPostForm)
    {
        return addPostService.add(addPostForm);
    }

    @PostMapping("/updateMyPost")
    public Result updateMyPost(UpdateMyPostForm updateMyPostForm)
    {
        return updateMyPostService.update(updateMyPostForm);
    }

    @PostMapping("/deleteMyPost")
    public Result deleteMyPost(IdForm idForm)
    {
        return deleteMyPostService.delete(idForm);
    }

    /**
     * 评论操作
     * @param addCommentForm {type:类型, id:postId或CommentId}
     */
    @PostMapping("/addComment")
    public Result addComment(AddCommentForm addCommentForm)
    {
        return addCommentService.comment(addCommentForm);
    }

    @PostMapping("/deleteMyComment")
    public Result deleteMyComment(IdForm idForm)
    {
        return deleteMyCommentService.delete(idForm);
    }

    @PostMapping("/postLike")
    public Result postLike(IdForm idForm)
    {
        return postLikeService.like(idForm);
    }

    @PostMapping("/postCollect")
    public Result postCollect(IdForm idForm)
    {
        return postCollectService.collect(idForm);
    }
}
