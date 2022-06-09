package com.poemSys.user.service.forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPost;
import com.poemSys.common.entity.connection.ConUserPost;
import com.poemSys.common.service.ConUserPostService;
import com.poemSys.common.service.SysPostService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.user.bean.Form.UpdateMyPostForm;
import com.poemSys.user.service.general.ImageUploadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UpdateMyPostService
{
    @Autowired
    SysPostService sysPostService;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    ConUserPostService conUserPostService;

    @Autowired
    ContentCheckService contentCheckService;

    @Autowired
    ImageUploadService imageUploadService;

    public Result update(UpdateMyPostForm updateMyPostForm)
    {
        Long userId = getLoginSysUserService.getSysUser().getId();
        long postId = updateMyPostForm.getId();
        String title = updateMyPostForm.getTitle();
        String content = updateMyPostForm.getContent();
        MultipartFile coverImage = updateMyPostForm.getCoverImage();
        String imagePath;

        SysPost sysPost = sysPostService.getById(postId);
        if(sysPost==null)
            return new Result(1, "帖子不存在,id:"+postId, null);

        //验证是不是本人的帖子
        ConUserPost con = conUserPostService.getOne(new QueryWrapper<ConUserPost>()
                .eq("user_id", userId).eq("post_id", postId));
        if(con==null)
            return new Result(-2, "权限不足,无法编辑非本人帖子,id:"+postId, null);

        if(!StringUtils.isBlank(title))
        {
            String titleCheckRes = contentCheckService.KMPCheckout(title);
            if(!titleCheckRes.equals("pass"))
                return new Result(1, "编辑失败,您的帖子标题含有敏感词:"+titleCheckRes, null);
            sysPost.setTitle(title);
        }
        if(!StringUtils.isBlank(content))
        {
            String contentCheckRes = contentCheckService.KMPCheckout(content);
            if(!contentCheckRes.equals("pass"))
                return new Result(1, "编辑失败,您的帖子内容含有敏感词:"+contentCheckRes, null);
            sysPost.setContent(content);
        }
        if(coverImage!=null)
        {
            Result uploadRes = imageUploadService.upload(coverImage, "/images/forum/");
            if (uploadRes.getCode() != 0)
                return uploadRes;
            imagePath = uploadRes.getData().toString();
            sysPost.setCoverImage(imagePath);
        }
        sysPostService.updateById(sysPost);
        return new Result(0, "帖子修改成功,id:"+postId, null);
    }
}
