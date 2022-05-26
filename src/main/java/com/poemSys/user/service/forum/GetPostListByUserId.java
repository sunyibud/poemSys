package com.poemSys.user.service.forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPost;
import com.poemSys.common.entity.connection.ConUserPost;
import com.poemSys.common.service.ConUserPostService;
import com.poemSys.common.service.SysPostService;
import com.poemSys.user.bean.Form.PageByIdForm;
import com.poemSys.user.bean.PostPageAns;
import com.poemSys.user.service.general.PostPageAnsProService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetPostListByUserId
{
    @Autowired
    ConUserPostService conUserPostService;

    @Autowired
    SysPostService sysPostService;

    @Autowired
    PostPageAnsProService postPageAnsProService;

    public Result get(PageByIdForm pageByIdForm)
    {
        long userId = pageByIdForm.getId();
        long page = pageByIdForm.getPage();
        long size = pageByIdForm.getSize();

        List<ConUserPost> con = conUserPostService.list(new QueryWrapper<ConUserPost>()
                .eq("user_id", userId));
        if(con.isEmpty())
            return new Result(0, "获取用户"+userId+"(id)的帖子成功,共0条", 0);
        List<Long> postIds = new ArrayList<>();
        con.forEach(c-> postIds.add(c.getPostId()));

        Page<SysPost> postPage = new Page<>(page, size);
        Page<SysPost> pageAns = sysPostService.page(postPage, new QueryWrapper<SysPost>()
                .in("post_id", postIds).orderByDesc("created_time"));

        PostPageAns finalAns = postPageAnsProService.pro(pageAns);

        return new Result(0, "获取用户"+userId+"(id)的帖子成功,共"+finalAns.getTotal()+"条", finalAns);
    }
}
