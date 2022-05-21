package com.poemSys.user.service.userInfo;

import cn.hutool.core.lang.UUID;
import com.poemSys.common.bean.Const;
import com.poemSys.common.bean.Result;
import com.poemSys.common.utils.CaptchaGenerate;
import com.poemSys.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendEmailService
{
    @Autowired
    CaptchaGenerate captchaGenerate;

    @Autowired
    EmailService emailService;

    @Autowired
    RedisUtil redisUtil;

    public Result send(String emailAddress)
    {
        String regex = "\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}";
        if (!emailAddress.matches(regex))
            return new Result(-3, "邮箱格式不合法", null);
        String emailCode = captchaGenerate.LetterNumVerification();
        boolean isSend = emailService.sendHtmlMail(emailAddress, "古诗文网", emailCode);
        if (!isSend)
            return new Result(-1, "系统异常，邮箱发送失败", null);
        String key = UUID.randomUUID().toString();
        redisUtil.hset(Const.EMAIL_KEY, key + emailAddress, emailCode, 180);
        return new Result(0, "邮箱验证发送成功，有效时间为3分钟", key);
    }
}
