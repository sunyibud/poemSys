package com.poemSys.user.service.userInfo;

import com.poemSys.common.bean.Const;
import com.poemSys.common.bean.IsOk;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.service.SysUserService;
import com.poemSys.common.service.general.GetLoginSysUserService;
import com.poemSys.common.utils.RedisUtil;
import com.poemSys.user.bean.Form.ResetPasswordForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class ResetPasswordService
{
    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    SysUserService sysUserService;

    public Result reset(ResetPasswordForm resetPasswordForm)
    {
        SysUser sysUser = getLoginSysUserService.getSysUser();
        String key = resetPasswordForm.getKey();
        String email = sysUser.getEmail();
        String emailCode = resetPasswordForm.getEmailCode();

        if (StringUtils.isBlank(emailCode) || StringUtils.isBlank(key))
        {
            log.info("邮箱验证码不能为空");
            return new Result(-3, "邮箱验证码为空", null);
        }

        IsOk isOk = new IsOk();
        Object object = redisUtil.hget(Const.EMAIL_KEY, key+email, isOk);

        if(!isOk.isOk())
            return new Result(-1, "服务器异常，redis操作失败", null);
        if (!emailCode.equals(object))
        {
            log.info("邮箱验证码错误");
            return new Result(1, "邮箱验证码有误", null);
        }

        String newPassword = resetPasswordForm.getNewPassword();
        String encode = bCryptPasswordEncoder.encode(newPassword);
        sysUser.setPassword(encode);
        sysUserService.updateById(sysUser);

        return new Result(0, "密码重置成功", null);
    }
}
