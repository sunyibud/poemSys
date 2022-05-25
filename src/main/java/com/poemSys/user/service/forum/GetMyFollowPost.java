package com.poemSys.user.service.forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPost;
import com.poemSys.common.entity.connection.ConUserFollow;
import com.poemSys.common.entity.connection.ConUserPost;
import com.poemSys.common.service.ConUserFollowService;
import com.poemSys.common.service.ConUserPostService;
import com.poemSys.common.service.SysPostService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.user.bean.PostPageAns;
import com.poemSys.user.service.general.PostPageAnsProService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页获取我的关注用户的发的帖子
 */
@Service
public class GetMyFollowPost
{
    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    ConUserFollowService conUserFollowService;

    @Autowired
    ConUserPostService conUserPostService;

    @Autowired
    SysPostService sysPostService;

    @Autowired
    PostPageAnsProService postPageAnsProService;

    public Result get(int page, int size)
    {
        Long userId = getLoginSysUserService.getSysUser().getId();

        List<ConUserFollow> con = conUserFollowService.list(new QueryWrapper<ConUserFollow>()
                .eq("follow_user_id", userId));
        if(con.isEmpty())
            return new Result(0, "获取关注用户帖子成功,共0条数据", 0);
        List<Long> beFollowUserIds = new ArrayList<>();
        con.forEach(c-> beFollowUserIds.add(c.getBeFollowUserId()));

        List<ConUserPost> con1 = conUserPostService.list(new QueryWrapper<ConUserPost>()
                .in("user_id", beFollowUserIds));
        if(con1.isEmpty())
            return new Result(0, "获取关注用户帖子成功,共0条数据", 0);

        List<Long> postIds = new ArrayList<>();
        con1.forEach(c-> postIds.add(c.getPostId()));

        Page<SysPost> postPage = new Page<>(page, size);
        Page<SysPost> pageAns = sysPostService.page(postPage, new QueryWrapper<SysPost>()
                .in("id", postIds));
        PostPageAns finalAns = postPageAnsProService.pro(pageAns);

        return new Result(0, "获取关注用户帖子成功,共"+finalAns.getTotal()+"条数据", finalAns);
    }
}
