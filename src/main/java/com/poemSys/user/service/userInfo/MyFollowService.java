package com.poemSys.user.service.userInfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.admin.bean.PageListRes;
import com.poemSys.admin.service.userManage.UserListInfoPageAnsProcessService;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.entity.connection.ConUserFollow;
import com.poemSys.common.service.ConUserFollowService;
import com.poemSys.common.service.SysUserService;
import com.poemSys.user.bean.UserInfoPageAns;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.user.service.general.UserInfoPageAnsProService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyFollowService
{
    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    ConUserFollowService conUserFollowService;

    @Autowired
    UserInfoPageAnsProService userInfoPageAnsProService;

    public Result getPageList(Integer page, Integer size)
    {
        Long id = getLoginSysUserService.getSysUser().getId();
        Page<SysUser> userPage = new Page<>(page, size);

        List<ConUserFollow> list = conUserFollowService.list(new QueryWrapper<ConUserFollow>()
                .eq("follow_user_id", id));
        List<Long> beFollowList = new ArrayList<>();
        list.forEach(l-> beFollowList.add(l.getBeFollowUserId()));
        if(beFollowList.isEmpty())
            return new Result(0, "分页获取用户粉丝列表成功,共0条", new PageListRes(
                    0L, size.longValue(), page.longValue(), 0L, null
            ));
        Page<SysUser> pageAns = sysUserService.page(userPage, new QueryWrapper<SysUser>()
                .in("id", beFollowList));
        UserInfoPageAns finalAns = userInfoPageAnsProService.pro(pageAns);
        return new Result(0, "分页获取用户关注列表成功,共"+finalAns.getTotal()+"条", finalAns);
    }
}
