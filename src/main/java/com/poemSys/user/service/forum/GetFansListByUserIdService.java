package com.poemSys.user.service.forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.entity.connection.ConUserFollow;
import com.poemSys.common.service.ConUserFollowService;
import com.poemSys.common.service.SysUserService;
import com.poemSys.user.bean.Form.PageByIdForm;
import com.poemSys.user.bean.UserInfoPageAns;
import com.poemSys.user.service.general.UserInfoPageAnsProService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *根据用户id获取该用户的粉丝列表
 */
@Service
public class GetFansListByUserIdService
{
    @Autowired
    ConUserFollowService conUserFollowService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    UserInfoPageAnsProService userInfoPageAnsProService;

    public Result get(PageByIdForm pageByIdForm)
    {
        long userId = pageByIdForm.getId();
        long page = pageByIdForm.getPage();
        long size = pageByIdForm.getSize();

        List<ConUserFollow> con = conUserFollowService.list(new QueryWrapper<ConUserFollow>()
                .eq("be_follow_user_id", userId));
        if(con.isEmpty())
            return new Result(0, "获取用户"+userId+"(id)的粉丝列表成功", null);
        List<Long> followUserIds = new ArrayList<>();
        con.forEach(c-> followUserIds.add(c.getBeFollowUserId()));

        Page<SysUser> userPage = new Page<>(page, size);
        Page<SysUser> pageAns = sysUserService.page(userPage,
                new QueryWrapper<SysUser>().in("user_id", followUserIds));
        UserInfoPageAns finalAns = userInfoPageAnsProService.pro(pageAns);
        return new Result(0, "获取用户"+userId+"(id)的粉丝列表成功", finalAns);
    }
}
