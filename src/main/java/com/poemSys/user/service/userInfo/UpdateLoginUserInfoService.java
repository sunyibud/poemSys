package com.poemSys.user.service.userInfo;

import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.service.SysUserService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.user.service.general.UpdateRedisSysUserService;
import com.poemSys.common.utils.JwtUtils;
import com.poemSys.common.utils.RedisUtil;
import com.poemSys.user.bean.Form.UpdateUserInfoForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateLoginUserInfoService
{
    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UpdateRedisSysUserService updateRedisSysUserService;

    public Result update(UpdateUserInfoForm newUserInfo)
    {

        SysUser sysUser = getLoginSysUserService.getSysUser();

        String oldUsername = sysUser.getUsername();
        String newUsername = newUserInfo.getUsername();

        //如果改了用户名
        if(!StringUtils.isBlank(newUsername)&&oldUsername.equals(newUsername))
        {
            SysUser sysUser1 = sysUserService.getSysUserByUsername(newUsername);
            if(sysUser1!=null)
                return new Result(1, "用户名已存在，无法修改", null);
        }
        if (!StringUtils.isBlank(newUsername))
            sysUser.setUsername(newUsername);
        if(!StringUtils.isBlank(newUserInfo.getSignature()))
            sysUser.setSignature(newUserInfo.getSignature());
        if(!StringUtils.isBlank(newUserInfo.getSex()))
            sysUser.setSex(newUserInfo.getSex());
        if(!StringUtils.isBlank(newUserInfo.getTelephone()))
            sysUser.setTelephone(newUserInfo.getTelephone());

        sysUserService.updateById(sysUser);

        updateRedisSysUserService.update();
        return new Result(0, "用户信息修改成功", null);
    }
}
