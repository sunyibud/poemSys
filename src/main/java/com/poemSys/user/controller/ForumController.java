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
import com.poemSys.user.service.general.ImageUploadService;
import com.poemSys.user.service.general.PostPageAnsProService;
import com.poemSys.user.service.general.SwapSysPostRecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    PostPageAnsProService postPageAnsProService;

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

    @Autowired
    ImageUploadService imageUploadService;

    @Autowired
    GetMyFollowPost getMyFollowPost;

    @PostMapping("/partPostList/{page}/{size}")
    public Result partPostList(@PathVariable("page") Integer page,
                               @PathVariable("size") Integer size)
    {
        Page<SysPost> postPage = new Page<>(page, size);
        Page<SysPost> pageAns = sysPostService.page(postPage);
        PostPageAns res = postPageAnsProService.pro(pageAns);
        return new Result(0, "分页获取帖子列表成功", res);
    }

    @PostMapping("/getPostById")
    public Result getPostById(@RequestBody IdForm idForm)
    {
        return getPostByIdService.get(idForm);
    }

    @PostMapping("/getCommentByPostId")
    public Result getCommentByPostId(@RequestBody IdForm idForm)
    {
        return getCommentByPostId.get(idForm);
    }

    @PostMapping("/getMyPost/{page}/{size}")
    public Result getMyPost(@PathVariable("page") Integer page,
                            @PathVariable("size") Integer size)
    {
        return getMyPostService.get(page, size);
    }

    /**
     * 新增帖子
     * @param addPostForm FormData格式
     */
    @PostMapping("/addPost")
    public Result addPost(AddPostForm addPostForm)
    {
        return addPostService.add(addPostForm);
    }

    @PostMapping("/updateMyPost")
    public Result updateMyPost(@RequestBody UpdateMyPostForm updateMyPostForm)
    {
        return updateMyPostService.update(updateMyPostForm);
    }

    @PostMapping("/deleteMyPost")
    public Result deleteMyPost(@RequestBody IdForm idForm)
    {
        return deleteMyPostService.delete(idForm);
    }

    /**
     * 评论操作
     * @param addCommentForm {type:类型, id:postId或CommentId}
     */
    @PostMapping("/addComment")
    public Result addComment(@RequestBody AddCommentForm addCommentForm)
    {
        return addCommentService.comment(addCommentForm);
    }

    @PostMapping("/deleteMyComment")
    public Result deleteMyComment(@RequestBody IdForm idForm)
    {
        return deleteMyCommentService.delete(idForm);
    }

    @PostMapping("/postLike")
    public Result postLike(@RequestBody IdForm idForm)
    {
        return postLikeService.like(idForm);
    }

    @PostMapping("/postCollect")
    public Result postCollect(@RequestBody IdForm idForm)
    {
        return postCollectService.collect(idForm);
    }

    @PostMapping("/imageUpload")
    public Result imageUpload(MultipartFile file)
    {
        return imageUploadService.upload(file, "/images/forum");
    }

    /**
     * 分页获取我关注用户发的帖子
     */
    @PostMapping("/getMyFollowPost/{page}/{size}")
    public Result myFollowPost(@PathVariable("page") Integer page,
                               @PathVariable("size") Integer size)
    {
        return getMyFollowPost.get(page, size);
    }
}
