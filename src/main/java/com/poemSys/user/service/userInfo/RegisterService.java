package com.poemSys.user.service.userInfo;

import com.poemSys.common.bean.Const;
import com.poemSys.common.bean.IsOk;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.service.SysUserService;
import com.poemSys.common.utils.RedisUtil;
import com.poemSys.user.bean.Form.UserRegisterForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class RegisterService
{
    @Autowired
    SysUserService sysUserService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    RedisUtil redisUtil;

    public Result register(UserRegisterForm userRegisterForm)
    {
        String key = userRegisterForm.getKey();
        String emailCode = userRegisterForm.getEmailCode();

        if (StringUtils.isBlank(emailCode) || StringUtils.isBlank(key))
        {
            log.info("邮箱验证码为空");
            return new Result(-3, "邮箱验证码不能为空", null);
        }
        IsOk isOk = new IsOk();
        Object object = redisUtil.hget(Const.EMAIL_KEY, key+userRegisterForm.getEmail(), isOk);

        if(!isOk.isOk())
            return new Result(-1, "服务器异常，redis操作失败", null);

        if (!emailCode.equals(object))
        {
            log.info("邮箱验证码错误或不匹配");
            return new Result(1, "邮箱验证码有误或邮箱与验证码不匹配", null);
        }

        log.info("input email code: "+emailCode);
        log.info("redis email code: "+object.toString());

        //判断用户名是否存在
        String username = userRegisterForm.getUsername();
        SysUser sysUser = sysUserService.getSysUserByUsername(username);
        if(sysUser != null)
            return new Result(1, "用户名已存在", null);

        //redis删除邮箱验证码
        if(!redisUtil.hdel(Const.EMAIL_KEY, key))
            return new Result(-1, "服务器异常，redis操作失败", null);

        //判断邮箱是否存在
        if(sysUserService.isEmailExist(userRegisterForm.getEmail()))
            return new Result(1, "邮箱已存在", null);

        String encodePassword = bCryptPasswordEncoder.encode(userRegisterForm.getPassword());
        SysUser newUser = new SysUser(username, encodePassword,
                "非淡泊无以明志，非宁静无以致远",
                userRegisterForm.getSex(),
                userRegisterForm.getEmail(),
                userRegisterForm.getTelephone(),
                "/images/headIcons/default.png", false,
                LocalDateTime.now(), true, null,
                null, null, 0, 0);
        sysUserService.save(newUser);
        return new Result(0, "注册成功", username);
    }
}
