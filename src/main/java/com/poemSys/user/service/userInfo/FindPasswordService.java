package com.poemSys.user.service.userInfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.common.bean.Const;
import com.poemSys.common.bean.IsOk;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.service.SysUserService;
import com.poemSys.common.utils.RedisUtil;
import com.poemSys.user.bean.Form.FindPasswordForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 未登录时找回（重置密码）
 */
@Slf4j
@Service
public class FindPasswordService
{
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public Result find(FindPasswordForm findPasswordForm)
    {
        String email = findPasswordForm.getEmailAddress();
        String key = findPasswordForm.getKey();
        String emailCode = findPasswordForm.getEmailCode();

        IsOk isOk = new IsOk();
        Object object = redisUtil.hget(Const.EMAIL_KEY, key+email, isOk);

        if(!isOk.isOk())
            return new Result(-1, "服务器异常，redis操作失败", null);
        if (!emailCode.equals(object))
        {
            log.info("找回密码邮箱验证码错误");
            return new Result(1, "邮箱验证码有误", null);
        }
        SysUser sysUser = sysUserService.getOne(new QueryWrapper<SysUser>().eq("email", email));
        if(sysUser==null)
            return new Result(1, "该邮箱还未注册", null);
        String newPassword = findPasswordForm.getNewPassword();
        sysUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
        sysUserService.updateById(sysUser);
        return new Result(0, "密码重置成功", null);
    }
}
