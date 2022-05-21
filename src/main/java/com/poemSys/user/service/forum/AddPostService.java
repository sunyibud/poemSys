package com.poemSys.user.service.forum;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPost;
import com.poemSys.common.entity.connection.ConUserPost;
import com.poemSys.common.service.ConUserPostService;
import com.poemSys.common.service.SysPostService;
import com.poemSys.common.service.general.GetLoginSysUserService;
import com.poemSys.user.bean.Form.AddPostForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AddPostService
{
    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    SysPostService sysPostService;

    @Autowired
    ConUserPostService conUserPostService;

    public Result add(AddPostForm addPostForm)
    {
        Long userId = getLoginSysUserService.getSysUser().getId();

        String title = addPostForm.getTitle();
        String content = addPostForm.getContent();
        String coverImage = addPostForm.getCoverImage();
        String uuid = UUID.randomUUID().toString();

        sysPostService.save(new SysPost(title, content, LocalDateTime.now(), 0,
                0, coverImage, uuid));
        SysPost sysPost = sysPostService.getOne(new QueryWrapper<SysPost>()
                .eq("uuid", uuid));
        conUserPostService.save(new ConUserPost(userId, sysPost.getId()));
        return new Result(0, "帖子发布成功", null);
    }
}
