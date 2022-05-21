package com.poemSys.user.service.userInfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.admin.bean.PageListRes;
import com.poemSys.admin.service.userManage.UserInfoPageAnsProcessService;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.entity.connection.ConUserFollow;
import com.poemSys.common.service.ConUserFollowService;
import com.poemSys.common.service.SysUserService;
import com.poemSys.common.service.general.GetLoginSysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class MyFansService
{
    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    ConUserFollowService conUserFollowService;

    @Autowired
    UserInfoPageAnsProcessService userInfoPageAnsProcessService;

    public Result getPageList(Integer page, Integer size)
    {
        Long id = getLoginSysUserService.getSysUser().getId();
        Page<SysUser> userPage = new Page<>(page, size);
        List<ConUserFollow> list = conUserFollowService.list(new QueryWrapper<ConUserFollow>()
                .eq("be_follow_user_id", id));
        List<Long> followList = new ArrayList<>();
        list.forEach(l->{
            followList.add(l.getFollowUserId());
        });
        if(followList.isEmpty())
            return new Result(0, "分页获取用户粉丝列表成功,共0条", new PageListRes(
                    0L, size.longValue(), page.longValue(), 0L, null
            ));
        Page<SysUser> pageAns = sysUserService.page(userPage, new QueryWrapper<SysUser>()
                .in("id", followList));
        PageListRes finalAns = userInfoPageAnsProcessService.pro(pageAns);
        return new Result(0, "分页获取用户粉丝列表成功,共"+finalAns.getTotal()+"条", finalAns);
    }
}
