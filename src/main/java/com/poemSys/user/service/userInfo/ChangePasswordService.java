package com.poemSys.user.service.userInfo;

import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.service.SysUserService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.user.service.general.UpdateRedisSysUserService;
import com.poemSys.user.bean.Form.ChangePasswordForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChangePasswordService
{
    @Autowired
    SysUserService sysUserService;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UpdateRedisSysUserService updateRedisSysUserService;

    public Result change(ChangePasswordForm passwordForm)
    {
        SysUser sysUser = getLoginSysUserService.getSysUser();

        String oldPassword = passwordForm.getOldPassword();
        String DbPassword = sysUser.getPassword();
        if(!bCryptPasswordEncoder.matches(oldPassword, DbPassword))
            return new Result(1, "输入密码有误", null);
        String newPassword = passwordForm.getNewPassword();
        if(StringUtils.isBlank(newPassword))
            return new Result(1, "新密码不能为空", null);
        String encodePassword = bCryptPasswordEncoder.encode(newPassword);
        sysUser.setPassword(encodePassword);
        sysUserService.updateById(sysUser);
        updateRedisSysUserService.update();
        return new Result(0, "用户密码修改成功", sysUser.getUsername());
    }
}
