package com.poemSys.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPost;
import com.poemSys.common.service.SysPostService;
import com.poemSys.user.bean.Form.AddPostForm;
import com.poemSys.user.bean.Form.CommentOpeForm;
import com.poemSys.user.service.forum.CommentOpeService;
import com.poemSys.user.service.forum.DeleteCommentOpeService;
import com.poemSys.user.service.forum.GetCommentByPostId;
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
    GetCommentByPostId getCommentByPostId;

    @Autowired
    CommentOpeService commentOpeService;

    @Autowired
    DeleteCommentOpeService deleteCommentOpeService;

    @PostMapping("/partPostList/{page}/{size}")
    public Result partPostList(@PathVariable("page") Integer page,
                               @PathVariable("size") Integer size)
    {
        Page<SysPost> postPage = new Page<>(page, size);
        Page<SysPost> pageAns = sysPostService.page(postPage);
        return new Result(0, "分页获取帖子列表成功", pageAns);
    }

    @PostMapping("/getPostById")
    public Result getPostById(IdForm idForm)
    {
        SysPost sysPost = sysPostService.getById(idForm.getId());
        return new Result(0, "帖子信息获取成功，id:"+sysPost.getId(), null);
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
        return new Result();
    }

    @PostMapping("/addPost")
    public Result addPost(AddPostForm addPostForm)
    {
        return new Result();
    }

    @PostMapping("/updateMyPostOpe")
    public Result updateMyPost()
    {
        return new Result();
    }

    @PostMapping("/deleteMyPostOpe")
    public Result deleteMyPostOpe(IdForm idForm)
    {
        return new Result();
    }

    /**
     * 评论操作
     * @param commentOpeForm {type:类型, id:postId或CommentId}
     * @return
     */
    @PostMapping("/commentOpe")
    public Result commentOpe(CommentOpeForm commentOpeForm)
    {
        return commentOpeService.comment(commentOpeForm);
    }

    @PostMapping("/deleteCommentOpe")
    public Result deleteCommentOpe(IdForm idForm)
    {
        return deleteCommentOpeService.delete(idForm);
    }

    @PostMapping("/postLikeOpe")
    public Result postLikeOpe(IdForm idForm)
    {
        return new Result();
    }

    @PostMapping("/postCollectOpe")
    public Result postCollectOpe(IdForm idForm)
    {
        return new Result();
    }

    @PostMapping("/commentLikeOpe")
    public Result commentLikeOpe(IdForm idForm)
    {
        return new Result();
    }

}
