package com.poemSys.admin.controller;

import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.admin.bean.Form.IdsForm;
import com.poemSys.admin.bean.Form.SearchForm;
import com.poemSys.admin.service.forumManage.SearchPostService;
import com.poemSys.admin.service.forumManage.UpdatePostService;
import com.poemSys.common.bean.Result;
import com.poemSys.common.service.SysPostService;
import com.poemSys.user.bean.Form.UpdateMyPostForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 论坛管理后台
 */
@RestController
@RequestMapping("/api/admin")
public class ForumManageController
{
    @Autowired
    SysPostService sysPostService;

    @Autowired
    UpdatePostService updatePostService;

    @Autowired
    SearchPostService searchPostService;

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/deletePost")
    public Result deletePost(@RequestBody IdForm idForm)
    {
        sysPostService.removeById(idForm.getId());
        return new Result(0, "帖子"+idForm.getId()+"删除成功", null);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/deletePosts")
    public Result deletePosts(@RequestBody IdsForm idsForm)
    {
        List<IdForm> ids = idsForm.getIds();
        ids.forEach(e-> sysPostService.removeById(e.getId()));
        return new Result(0, "帖子批量删除成功,共删除"+ids.size()+"条数据", null);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/updatePost")
    public Result updatePost(UpdateMyPostForm updateMyPostForm)
    {
       return updatePostService.update(updateMyPostForm);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/searchPost")
    public Result searchPost(@RequestBody SearchForm searchForm)
    {
        return searchPostService.search(searchForm);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/lockPost")
    public Result lockPost(@RequestBody SearchForm searchForm)
    {
        return new Result();
    }
}
