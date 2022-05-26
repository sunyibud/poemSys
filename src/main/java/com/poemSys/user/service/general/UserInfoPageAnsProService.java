package com.poemSys.user.service.general;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.common.entity.basic.SysPoem;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.user.bean.PoemPageAns;
import com.poemSys.user.bean.SysPoemRes;
import com.poemSys.user.bean.UserInfo;
import com.poemSys.user.bean.UserInfoPageAns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 前台系统分页返回用户信息时的加工处理
 */
@Service
public class UserInfoPageAnsProService
{
    @Autowired
    SwapUserInfoService swapUserInfoService;

    public UserInfoPageAns pro(Page<SysUser> pageAns)
    {
        List<SysUser> records = pageAns.getRecords();
        List<UserInfo> newRecords = new ArrayList<>();
        records.forEach(r-> newRecords.add(swapUserInfoService.swap(r)));
        return new UserInfoPageAns(pageAns.getTotal(), pageAns.getSize(),
                pageAns.getCurrent(), pageAns.getPages(), newRecords);
    }
}
