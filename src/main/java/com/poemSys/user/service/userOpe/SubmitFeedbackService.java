package com.poemSys.user.service.userOpe;

import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysFeedback;
import com.poemSys.common.service.SysFeedbackService;
import com.poemSys.user.bean.Form.ContentForm;
import com.poemSys.user.service.forum.ContentCheckService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SubmitFeedbackService
{
    @Autowired
    SysFeedbackService sysFeedbackService;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    ContentCheckService contentCheckService;

    public Result submit(ContentForm contentForm)
    {
        Long submitUserId = getLoginSysUserService.getSysUser().getId();
        String content = contentForm.getContent();

        String checkout = contentCheckService.KMPCheckout(content);
        if(!checkout.equals("pass"))
            return new Result(1, "您的反馈信息中包含敏感词:"+checkout, null);

        sysFeedbackService.save(new SysFeedback(submitUserId, content, false, LocalDateTime.now(),
                null, null, 0));

        return new Result(0, "提交成功", null);
    }
}
