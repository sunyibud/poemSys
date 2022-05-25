package com.poemSys.user.service.forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPost;
import com.poemSys.common.entity.connection.ConUserPost;
import com.poemSys.common.service.ConUserPostService;
import com.poemSys.common.service.SysPostService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.user.bean.PostPageAns;
import com.poemSys.user.service.general.PostPageAnsProService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetMyPostService
{
    @Autowired
    ConUserPostService conUserPostService;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    PostPageAnsProService postPageAnsProService;

    @Autowired
    SysPostService sysPostService;
    public Result get(int page, int size)
    {
        Long userId = getLoginSysUserService.getSysUser().getId();

        List<ConUserPost> conList = conUserPostService.list(new QueryWrapper<ConUserPost>()
                .eq("user_id", userId));
        if(conList.isEmpty())
            return new Result(0, "用户"+userId+"(id)没有发布帖子", null);

        List<Long> postIds = new ArrayList<>();
        conList.forEach(c-> postIds.add(c.getPostId()));
        Page<SysPost> postPage = new Page<>(page, size);
        Page<SysPost> pageAns = sysPostService.page(postPage, new QueryWrapper<SysPost>()
                .in("id", postIds));
        PostPageAns res = postPageAnsProService.pro(pageAns);
        return new Result(0, "分页获取我的帖子列表成功", res);
    }
}
