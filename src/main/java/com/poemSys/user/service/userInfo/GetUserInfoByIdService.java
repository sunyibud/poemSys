package com.poemSys.user.service.userInfo;

import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.service.SysUserService;
import com.poemSys.user.bean.UserInfo;
import com.poemSys.user.service.general.SwapUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetUserInfoByIdService
{
    @Autowired
    SysUserService sysUserService;

    @Autowired
    SwapUserInfoService swapUserInfoService;

    public Result get(IdForm idForm)
    {
        long userId = idForm.getId();
        SysUser sysUser = sysUserService.getById(userId);

        if(sysUser == null)
            return new Result(1, "用户不存在,id:"+userId, null);

        UserInfo userInfo = swapUserInfoService.swap(sysUser);
        return new Result(0, "获取用户信息成功", userInfo);
    }
}
